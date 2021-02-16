package ru.javaops.masterjava.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class MailSender {

    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(new File("c:/project/mail.conf"))) {

            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            Email email = new SimpleEmail();

            email.setHostName(prop.getProperty("mail.host"));
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator(prop.getProperty("mail.username"),
                    prop.getProperty("mail.password")));


            email.setSSLOnConnect(true);
            email.setTLS(false);
            email.setDebug(true);
            // Sender
            email.setFrom(prop.getProperty("mail.fromName"));

            // Email title
            email.setSubject(subject);

            // Email message.
            email.setMsg(body);

           String addresses = to.stream().map(Addressee::getEmail).collect(Collectors.joining(","));
            email.addTo(addresses);
            if (cc !=null ){
                if (!cc.isEmpty()){
                    String copyTo = cc.stream().map(Addressee::getEmail).collect(Collectors.joining(","));
                    email.addCc(copyTo);
                }

            }
          email.send();
            log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));

        }catch (EmailException e){
            e.printStackTrace();
        }

    }
}
