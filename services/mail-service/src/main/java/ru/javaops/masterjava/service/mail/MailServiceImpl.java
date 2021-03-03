package ru.javaops.masterjava.service.mail;

import ru.javaops.web.WebStateException;

import javax.jws.WebService;
import javax.xml.ws.soap.MTOM;
import java.io.File;
import java.io.IOException;
import java.util.Set;

@MTOM
@WebService(endpointInterface = "ru.javaops.masterjava.service.mail.MailService", targetNamespace = "http://mail.javaops.ru/"
//          , wsdlLocation = "WEB-INF/wsdl/mailService.wsdl"
)
public class MailServiceImpl implements MailService {
    public String sendToGroup(Set<Addressee> to, Set<Addressee> cc, String subject, String body) throws WebStateException {
        return MailSender.sendToGroup(to, cc, subject, body);
    }

    @Override
    public GroupResult sendBulk(Set<Addressee> to, String subject, String body) throws WebStateException {
        return MailServiceExecutor.sendBulk(to, subject, body);
    }

    @Override
    public File upload(String name) throws WebStateException {

        return new File(name);

    }
}