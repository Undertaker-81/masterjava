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
import java.io.*;
import java.util.ArrayList;
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
        List<User> result = new ArrayList<>();
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(inputStream)) {
            XMLStreamReader reader = processor.getReader();
            StringBuilder sb = new StringBuilder();
            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLEvent.START_ELEMENT) {
                    if ("User".equals(reader.getLocalName())) {
                        sb.append(reader.getAttributeLocalName(2)).append(":").append(reader.getAttributeValue(2)).append("|");
                    }
                    if ("fullName".equals(reader.getLocalName())){
                        sb.append(reader.getElementText()).append("|");
                    }
                    if ("project".equals(reader.getLocalName())){
                        sb.append(reader.getElementText()).append("|");
                    }
                }
            }
            String[] users = sb.toString().split("email:");

            for (String user : users){
                if (user.contains(projectName.value())){
                    String[] info = user.split("\\|");
                    User newUser = new User();
                    newUser.setEmail(info[0]);
                    newUser.setFullName(info[1]);
                    result.add(newUser);
                }
            }
        }
        return result.stream()
                                .sorted(Comparator.comparing(User::getFullName))
                                .collect(Collectors.toList());
    }

    public void createHTML(List<User> users) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("./src/test/java/ru/javaops/masterjava/xml/table.html", "UTF-8");
        writer.println("<!DOCTYPE html>");
        writer.println("<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>");
        writer.println("<table>\n" +
                "<tr><th>Full name</th><th>email</th></tr>");
        for (User user : users){
            writer.println("<tr><td>" + user.getFullName()+ "</td><td>"+ user.getEmail() + "</td></tr> ");

        }

        writer.println("</table>");
        writer.println("</body>\n" +
                "</html>");
        writer.close();
    }


}
