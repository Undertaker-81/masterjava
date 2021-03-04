package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.web.WebStateException;

import javax.activation.DataHandler;
import java.io.File;
import java.net.MalformedURLException;

@Slf4j
public class MailWSClientMain {
    public static void main(String[] args) throws WebStateException, MalformedURLException {
        String state = MailWSClient.sendToGroup(
                ImmutableSet.of(new Addressee("To <undertaker-81@mail.ru>")),
                ImmutableSet.of(new Addressee("Copy <undertaker-81@mail.ru>")), "Subject", "Body",
                ImmutableList.of(new Attachment("pgadmin.log", new DataHandler(new File("/home/dmitry/pgadmin.log").toURI().toURL()))));
        System.out.println(state);
    }
}