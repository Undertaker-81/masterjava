package ru.javaops.masterjava.service.mail;

import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.mail.dao.MailResultDao;
import ru.javaops.masterjava.service.mail.model.SendResult;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class MailSender {
  private static final   Properties prop = new Properties();


    static {
        try (InputStream input = new FileInputStream(new File("c:/project/mail.conf"))) {
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static final String SERVER = prop.getProperty("mail.host");
    private static final int PORT = Integer.parseInt(prop.getProperty("mail.port"));
    private static final String USER = prop.getProperty("mail.username");
    private static final String PASSWORD = prop.getProperty("mail.password");
    private static final boolean SSL = Boolean.parseBoolean(prop.getProperty("mail.useSSL"));
    private static final boolean DEBUG = Boolean.parseBoolean(prop.getProperty("mail.debug"));
    private static final String FROM = prop.getProperty("mail.fromName");

    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        MailResultDao dao = DBIProvider.getDao(MailResultDao.class);
        try {
            Email email = new SimpleEmail();

            email.setHostName(SERVER);
            email.setSmtpPort(PORT);
            email.setAuthenticator(new DefaultAuthenticator(USER, PASSWORD));
            email.setSSLOnConnect(SSL);
            email.setTLS(false);
            email.setDebug(DEBUG);
            // Sender
            email.setFrom(FROM);

            // Email title
            email.setSubject(subject);

            // Email message.
            email.setMsg(body);

            List<InternetAddress> internetAddressList = to.stream().map(addressee -> {
                try {
                  return    new InternetAddress(addressee.getEmail());
                } catch (AddressException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());

            email.setTo(internetAddressList);

            if (cc.size() > 0 ){
                List<InternetAddress> ccList = cc.stream().map(addressee -> {
                    try {
                        return    new InternetAddress(addressee.getEmail());
                    } catch (AddressException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList());
                email.setCc(ccList);

            }
          email.send();
            System.out.println("mails: " + email.getToAddresses());
            List<SendResult> sendResults = email.getToAddresses().stream()
                                                                    .map(mail -> new SendResult(LocalDateTime.now(), mail.getAddress(), true)).collect(Collectors.toList());

            dao.insertBatch(sendResults, sendResults.size());
            log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));

        }catch (EmailException e){
            List<SendResult> sendResults = to.stream()
                                        .map(addressee ->  new SendResult(LocalDateTime.now(), addressee.getEmail(),  false)).collect(Collectors.toList());
            dao.insertBatch(sendResults, sendResults.size());
            e.printStackTrace();
        }

    }
}
