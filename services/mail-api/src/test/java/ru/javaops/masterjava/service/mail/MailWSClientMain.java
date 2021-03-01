package ru.javaops.masterjava.service.mail;


import com.google.common.collect.ImmutableSet;

public class MailWSClientMain {
    public static void main(String[] args) {
     //   System.out.println(ClassLoader.class.getResource("/wsdl/mailService.wsdl").getPath());

        MailWSClient.sendToGroup(
                ImmutableSet.of(new Addressee("To <undertaker-81@mail.ru>")),
                ImmutableSet.of(new Addressee("Copy <undertaker-81@mail.ru>")), "Subject", "Body");
    }
}