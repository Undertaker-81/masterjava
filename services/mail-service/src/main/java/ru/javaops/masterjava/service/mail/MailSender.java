package ru.javaops.masterjava.service.mail;
import com.google.common.collect.ImmutableSet;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.FileUtils;
import ru.javaops.masterjava.ExceptionType;
import ru.javaops.masterjava.config.Configs;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.mail.persist.MailCase;
import ru.javaops.masterjava.service.mail.persist.MailCaseDao;
import ru.javaops.masterjava.web.WebStateException;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;;
import java.util.Set;

@Slf4j
public class MailSender {
    private static final MailCaseDao MAIL_CASE_DAO = DBIProvider.getDao(MailCaseDao.class);
    private static Config conf = Configs.getConfig("mail.conf", "mail");

    static MailResult sendTo(Addressee to, String subject, String body, List<Attachment> attachments) throws WebStateException {
        val state = sendToGroup(ImmutableSet.of(to), ImmutableSet.of(), subject, body, attachments);
        return new MailResult(to.getEmail(), state);
    }

    static String sendToGroup(Set<Addressee> to, Set<Addressee> cc, String subject, String body, List<Attachment> attachments) throws WebStateException {
        log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
        String state = MailResult.OK;
        try {

             val email = MailConfig.createHtmlEmail();

            Message message = new MimeMessage(email.getMailSession());
            message.setFrom(new InternetAddress(conf.getString("username")));
            message.setSubject(subject);
            for (Addressee addressee : to) {
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(addressee.getEmail()));
            }
            for (Addressee addressee : cc) {
                message.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(addressee.getEmail()));
            }

            // creates message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);


            for (Attachment attach : attachments) {
                MimeBodyPart attachPart = new MimeBodyPart();
                File file = File.createTempFile("attach","");
                FileUtils.copyInputStreamToFile(attach.getDataHandler().getInputStream(), file);
                attachPart.attachFile(file);
            //   attachPart.setDataHandler(attach.getDataHandler());
                attachPart.setFileName(attach.getName());
                multipart.addBodyPart(attachPart);
            }
            message.addHeader("List-Unsubscribe", "<mailto:masterjava@javaops.ru?subject=Unsubscribe&body=Unsubscribe>");
            message.setContent(multipart);
            Transport.send(message);


          //  email.send();
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
