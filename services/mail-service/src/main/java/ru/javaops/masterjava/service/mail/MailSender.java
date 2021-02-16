package ru.javaops.masterjava.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

@Slf4j
public class MailSender {

    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        Properties prop = new Properties();
        try (InputStream input = MailSender.class.getClassLoader().getResourceAsStream("mail.conf")) {

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            //get the property value and print it out
            System.out.println(prop.getProperty("mail.host"));
            System.out.println(prop.getProperty("mail.port"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Email email = new SimpleEmail();



        log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
    }
}
