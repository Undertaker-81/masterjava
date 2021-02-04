package ru.javaops.masterjava.upload;

import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.JaxbUnmarshaller;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserProcessor {
    private static final JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);

    public List<User> process(final InputStream is) throws XMLStreamException, JAXBException {
        final StaxStreamProcessor processor = new StaxStreamProcessor(is);
        List<User> users = new ArrayList<>();

        JaxbUnmarshaller unmarshaller = jaxbParser.createUnmarshaller();
        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
            ru.javaops.masterjava.xml.schema.User xmlUser = unmarshaller.unmarshal(processor.getReader(), ru.javaops.masterjava.xml.schema.User.class);
            final User user = new User(xmlUser.getValue(), xmlUser.getEmail(), UserFlag.valueOf(xmlUser.getFlag().value()));
            users.add(user);
        }
        return users;
    }

    public List<User> processByStax(final InputStream is, int countChank, UserDao dao) throws InterruptedException {
        final ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Callable<Void>> task = new ArrayList<>();
        List<User> users = new ArrayList<>();
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(is)) {
            XMLStreamReader reader = processor.getReader();


            while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                if ("User".equals(reader.getLocalName())) {
                User user = new User();

                    user.setFlag(UserFlag.valueOf(reader.getAttributeValue(0)));
                    user.setEmail(reader.getAttributeValue(2));
                    user.setFullName(reader.getElementText());
                    users.add(user);

                    if (users.size() == countChank){
                        task.add(() -> {
                            dao.insertBatch(users.iterator(), countChank);
                            return null;
                        });

                        users.clear();
                    }
                }

            }
        } catch (XMLStreamException e){

        }
        executor.invokeAll(task);
        executor.shutdown();
        return users;
    }
}
