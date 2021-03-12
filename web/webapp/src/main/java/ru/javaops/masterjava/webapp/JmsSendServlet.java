package ru.javaops.masterjava.webapp;

import lombok.extern.slf4j.Slf4j;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.IllegalStateException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/sendJms")
@Slf4j
@MultipartConfig
public class JmsSendServlet extends HttpServlet {
    private Connection connection;
    private Session session;
    private MessageProducer producer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            InitialContext initCtx = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) initCtx.lookup("java:comp/env/jms/ConnectionFactory");
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer((Destination) initCtx.lookup("java:comp/env/jms/queue/MailQueue"));
        } catch (Exception e) {
            throw new IllegalStateException("JMS init failed", e);
        }
    }

    @Override
    public void destroy() {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException ex) {
                log.warn("Couldn't close JMSConnection: ", ex);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result;
        try {
            log.info("Start sending");
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            String users = req.getParameter("users");
            String subject = req.getParameter("subject");
            String body = req.getParameter("body");
            Part filePart = req.getPart("attach");
            result = sendJms(users, subject, body, filePart);
            log.info("Processing finished with result: {}", result);
        } catch (Exception e) {
            log.error("Processing failed", e);
            result = e.toString();
        }
        resp.getWriter().write(result);
    }

    private synchronized String sendJms(String users, String subject, String body, Part attach) throws JMSException, IOException {
        ObjectMessage objectMessage = session.createObjectMessage();
       // BytesMessage bytesMessage = session.createBytesMessage();
        objectMessage.setObjectProperty("subject",subject);
        objectMessage.setObjectProperty("body", body);
        objectMessage.setObjectProperty("users", users);
        if (attach != null){
            objectMessage.setObjectProperty("filename", attach.getSubmittedFileName());
            //можно передать только примитивы, строки, листы, мапы из них, фигня конечно, но работает, потом найду "правильный" вариант
            int b ;
            List<Byte> bytes = new ArrayList<>();
            InputStream inputStream = attach.getInputStream();
            while (inputStream.available() > 0){
                bytes.add((byte) inputStream.read());
            }
            objectMessage.setObjectProperty("attach", bytes);
        }


   //     bytesMessage.setObjectProperty("attach", attach.getInputStream());
        producer.send(objectMessage);

        return "Successfully sent JMS message";
    }
}