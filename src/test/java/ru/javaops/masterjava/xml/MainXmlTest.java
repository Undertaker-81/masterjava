package ru.javaops.masterjava.xml;


import org.junit.Test;
import ru.javaops.masterjava.xml.schema.ProjectName;
import ru.javaops.masterjava.xml.schema.User;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;

/**
 * @author Dmitriy Panfilov
 * 27.01.2021
 */
public class MainXmlTest {

    @Test
     public void getUser() throws JAXBException, IOException {
        MainXml mainXml = new MainXml(ProjectName.TOP_JAVA);
        List<User> users = mainXml.getUsersWithJAXB();

    }

    @Test
    public void getUsersWithStax() throws IOException, XMLStreamException {
        MainXml mainXml = new MainXml(ProjectName.MASTER_JAVA);
        List<User> users = mainXml.getUsersWithStax();
        users.forEach(System.out::println);
    }

    @Test
    public void createHtml() throws IOException, XMLStreamException {
        MainXml mainXml = new MainXml(ProjectName.MASTER_JAVA);
        mainXml.createHTML(mainXml.getUsersWithStax());
    }
}