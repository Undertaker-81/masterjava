package ru.javaops.masterjava.xml;


import org.junit.Test;
import ru.javaops.masterjava.xml.schema.ProjectName;
import ru.javaops.masterjava.xml.schema.User;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

/**
 * @author Dmitriy Panfilov
 * 27.01.2021
 */
public class MainXmlTest {

    @Test
     public void getUser() throws JAXBException, IOException {
        MainXml mainXml = new MainXml(ProjectName.MASTER_JAVA);
        List<User> users = mainXml.getUser();
        users.forEach(System.out::println);
    }
}