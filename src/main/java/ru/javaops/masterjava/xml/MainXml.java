package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbMarshaller;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
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

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    private final ProjectName projectName;

    public MainXml(ProjectName project){
        this.projectName = project;
    }

    public ProjectName getProjectName() {
        return projectName;
    }

    public List<User> getUser() throws JAXBException, IOException {
        InputStream inputStream = Resources.getResource("payload.xml").openStream();

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


}
