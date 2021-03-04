package ru.javaops.masterjava.service.mail;

import com.sun.xml.internal.ws.developer.StreamingAttachment;
import com.sun.xml.ws.developer.StreamingDataHandler;
import ru.javaops.web.WebStateException;

import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.MTOM;
import java.io.File;
import java.io.IOException;
import java.util.Set;

@MTOM

@WebService(endpointInterface = "ru.javaops.masterjava.service.mail.MailService", targetNamespace = "http://mail.javaops.ru/"
//          , wsdlLocation = "WEB-INF/wsdl/mailService.wsdl"
)
public class MailServiceImpl implements MailService {
    public String sendToGroup(Set<Addressee> to, Set<Addressee> cc, String subject, String body, String filename) throws WebStateException {
        return MailSender.sendToGroup(to, cc, subject, body, filename);
    }

    @Override
    public GroupResult sendBulk(Set<Addressee> to, String subject, String body, String filename) throws WebStateException {
        return MailServiceExecutor.sendBulk(to, subject, body, filename);
    }

    @Override
    public void upload(String fileName, DataHandler data) {

        try {
            StreamingDataHandler dh = (StreamingDataHandler) data;
            File file = File.createTempFile(fileName, "");
            dh.moveTo(file);
            dh.close();
        } catch (Exception e) {
            throw new WebServiceException(e);
        }
    }

}