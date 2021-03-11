package ru.javaops.masterjava.service.mail.listeners;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.service.mail.MailWSClient;
import ru.javaops.masterjava.service.mail.util.Attachments;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@WebListener
@Slf4j
public class JmsMailListener implements ServletContextListener {
    private Thread listenerThread = null;
    private QueueConnection connection;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            InitialContext initCtx = new InitialContext();
            QueueConnectionFactory connectionFactory =
                    (QueueConnectionFactory) initCtx.lookup("java:comp/env/jms/ConnectionFactory");
            connection = connectionFactory.createQueueConnection();
            QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue) initCtx.lookup("java:comp/env/jms/queue/MailQueue");
            QueueReceiver receiver = queueSession.createReceiver(queue);
            connection.start();
            log.info("Listen JMS messages ...");
            listenerThread = new Thread(() -> {
                try {
                    while (!Thread.interrupted()) {
                        Message m = receiver.receive();
                        // TODO implement mail sending

                        if (m instanceof ObjectMessage) {
                            ObjectMessage objectMessage = (ObjectMessage) m;
                            String subject = (String) objectMessage.getObjectProperty("subject");
                            String body = (String) objectMessage.getObjectProperty("body");
                            String users = (String) objectMessage.getObjectProperty("users");
                            String filename = (String) objectMessage.getObjectProperty("filename");

                            List<Byte> bytes = (List<Byte>) objectMessage.getObjectProperty("attach");
                            byte[] b = new byte[bytes.size()];
                            for (int i = 0;  i < bytes.size(); i++){
                                b[i] = bytes.get(i);
                            }
                            InputStream inputStream = new ByteArrayInputStream(b);
                            MailWSClient.sendBulk(MailWSClient.split(users), subject, body, ImmutableList.of(Attachments.getAttachment(filename, inputStream )));
                            log.info("Received TextMessage with text '{}'", body);
                        }
                    }
                } catch (Exception e) {
                    log.error("Receiving messages failed: " + e.getMessage(), e);
                }
            });
            listenerThread.setName("JMS Listener Thread");
            listenerThread.start();
        } catch (Exception e) {
            log.error("JMS failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException ex) {
                log.warn("Couldn't close JMSConnection: ", ex);
            }
        }
        if (listenerThread != null) {
            listenerThread.interrupt();
        }
    }
}