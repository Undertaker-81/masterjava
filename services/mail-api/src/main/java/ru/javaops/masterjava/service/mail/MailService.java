package ru.javaops.masterjava.service.mail;

import com.sun.xml.internal.ws.developer.StreamingAttachment;
import ru.javaops.web.WebStateException;

import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.ws.soap.MTOM;
import java.io.File;
import java.util.Set;

@MTOM
@StreamingAttachment(parseEagerly=true, memoryThreshold=0)
@WebService(targetNamespace = "http://mail.javaops.ru/")
//@SOAPBinding(
//        style = SOAPBinding.Style.DOCUMENT,
//        use= SOAPBinding.Use.LITERAL,
//        parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface MailService {

    @WebMethod
    String sendToGroup(
            @WebParam(name = "to") Set<Addressee> to,
            @WebParam(name = "cc") Set<Addressee> cc,
            @WebParam(name = "subject") String subject,
            @WebParam(name = "body") String body,
            @WebParam(name = "attach") String filename) throws WebStateException;

    @WebMethod
    GroupResult sendBulk(
            @WebParam(name = "to") Set<Addressee> to,
            @WebParam(name = "subject") String subject,
            @WebParam(name = "body") String body,
            @WebParam(name = "attach") String filename) throws WebStateException;

    @WebMethod
    void upload(String fileName,
                @XmlMimeType("application/octet-stream")
                        DataHandler data);



}