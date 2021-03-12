package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.IOUtils;
import ru.javaops.masterjava.ExceptionType;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.mail.persist.MailCase;
import ru.javaops.masterjava.service.mail.persist.MailCaseDao;
import ru.javaops.masterjava.web.WebStateException;

import javax.mail.Multipart;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@Slf4j
public class MailSender {
    private static final MailCaseDao MAIL_CASE_DAO = DBIProvider.getDao(MailCaseDao.class);

    static MailResult sendTo(Addressee to, String subject, String body, List<Attachment> attachments) throws WebStateException {
        val state = sendToGroup(ImmutableSet.of(to), ImmutableSet.of(), subject, body, attachments);
        return new MailResult(to.getEmail(), state);
    }

    static String sendToGroup(Set<Addressee> to, Set<Addressee> cc, String subject, String body, List<Attachment> attachments) throws WebStateException {
        log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
        String state = MailResult.OK;
        try {
            val email = MailConfig.createHtmlEmail();
            email.setSubject(subject);
            email.setHtmlMsg(body);
            for (Addressee addressee : to) {
                email.addTo(addressee.getEmail(), addressee.getName());
            }
            for (Addressee addressee : cc) {
                email.addCc(addressee.getEmail(), addressee.getName());
            }
           // email.buildMimeMessage();
            MimeMessage mimeMessage = new MimeMessage(email.getMailSession());
            for (Attachment attach : attachments) {
                InternetHeaders fileHeaders = new InternetHeaders();
                fileHeaders.setHeader("Content-Transfer-Encoding", "8bit");
                fileHeaders.setHeader("Content-Type", attach.getDataHandler().getContentType() + "; " + attach.getDataHandler().getName());
                Multipart multipart = new MimeMultipart();
                byte[] fileByteArray = IOUtils.toByteArray(attach.getDataHandler().getInputStream());
//and convert to Base64
                byte[] fileBase64ByteArray = java.util.Base64.getEncoder().encode(fileByteArray);
                MimeBodyPart bodyPart = new MimeBodyPart(fileHeaders, fileBase64ByteArray);
                bodyPart.setFileName(attach.getDataHandler().getName());
                multipart.addBodyPart(bodyPart);
                mimeMessage.setContent( multipart);


              //  email.attach(attach.getDataHandler().getDataSource(), encodeWord(attach.getName()), null);
            }

            //  https://yandex.ru/blog/company/66296
            email.setHeaders(ImmutableMap.of("List-Unsubscribe", "<mailto:masterjava@javaops.ru?subject=Unsubscribe&body=Unsubscribe>"));
            email.buildMimeMessage();
            email.sendMimeMessage();
           // email.send();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            state = e.getMessage();
        }
        try {
            MAIL_CASE_DAO.insert(MailCase.of(to, cc, subject, state));
        } catch (Exception e) {
            log.error("Mail history saving exception", e);
            throw new WebStateException(e, ExceptionType.DATA_BASE);
        }
        log.info("Sent with state: " + state);
        return state;
    }

    public static String encodeWord(String word) throws UnsupportedEncodingException {
        if (word == null) {
            return null;
        }
        return MimeUtility.encodeWord(word, StandardCharsets.UTF_8.name(), null);
    }
}
