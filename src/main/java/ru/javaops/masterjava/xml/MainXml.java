package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbMarshaller;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Dmitriy Panfilov
 * 27.01.2021
 */
public class MainXml {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    private final InputStream inputStream = Resources.getResource("payload.xml").openStream();

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    private final ProjectName projectName;

    public MainXml(ProjectName project) throws IOException {
        this.projectName = project;
    }

    public ProjectName getProjectName() {
        return projectName;
    }

    public List<User> getUsersWithJAXB() throws JAXBException {
        Payload userElement = JAXB_PARSER.unmarshal(inputStream);
        List<User> users = userElement.getUsers().getUser()
                                                            .stream()
                                                            .filter(user -> user.getGroups().getGroup().stream()
                                                                    .anyMatch(
                                                                            group ->
                                                                                    group.getProject().equals(projectName)) )
                                                            .sorted(Comparator.comparing(User::getFullName))
                                                            .collect(Collectors.toList());
        return users;
    }

    public List<User> getUsersWithStax() throws XMLStreamException {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(inputStream)) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLEvent.START_ELEMENT) {
                    System.out.println(reader.getName());
                    if ("User".equals(reader.getLocalName())) {

                        System.out.println(reader.getAttributeLocalName(1));
                    }
                }
            }
        }
        return null;
    }

}
