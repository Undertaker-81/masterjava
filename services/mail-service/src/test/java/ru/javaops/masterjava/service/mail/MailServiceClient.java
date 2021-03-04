package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import com.sun.xml.ws.developer.JAXWSProperties;
import ru.javaops.web.WebStateException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.MTOMFeature;
import javax.xml.ws.soap.SOAPBinding;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class MailServiceClient {

    public static void main(String[] args) throws MalformedURLException, WebStateException {
        Service service = Service.create(
                new URL("http://localhost:8080/mail/mailService?wsdl"),
                new QName("http://mail.javaops.ru/", "MailServiceImplService"));

        MailService mailService = service.getPort(MailService.class,new MTOMFeature(true,40000));
//        BindingProvider bindingProvider = (BindingProvider) mailService;
//        SOAPBinding sopadBinding = (SOAPBinding) bindingProvider.getBinding();
//        sopadBinding.setMTOMEnabled(true);




        String state = mailService.sendToGroup(ImmutableSet.of(new Addressee("undertaker-81@mail.ru", null)), null,
                "Group mail subject", "Group mail body",ImmutableList.of(new Attachment("pgadmin.log", new DataHandler(new File("/home/dmitry/pgadmin.log").toURI().toURL()))) );
        System.out.println("Group mail state: " + state);

        GroupResult groupResult = mailService.sendBulk(ImmutableSet.of(
                new Addressee("Мастер Java <undertaker-81@mail.ru>"),
                new Addressee("Bad Email <bad_email.ru>")), "Bulk mail subject", "Bulk mail body", null);
        System.out.println("\nBulk mail groupResult:\n" + groupResult);

    }


}
