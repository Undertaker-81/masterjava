package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.web.WebStateException;

@Slf4j
public class MailWSClientMain {
    public static void main(String[] args) throws WebStateException {
        String state = MailWSClient.sendToGroup(
                ImmutableSet.of(new Addressee("To <undertaker-81@mail.ru>")),
                ImmutableSet.of(new Addressee("Copy <undertaker-81@mail.ru>")), "Subject", "Body", "/home/dmitry/pgadmin.log");
        System.out.println(state);
    }
}