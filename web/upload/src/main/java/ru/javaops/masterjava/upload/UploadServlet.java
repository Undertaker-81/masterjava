package ru.javaops.masterjava.upload;

import com.google.common.collect.ImmutableList;
import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static ru.javaops.masterjava.common.web.ThymeleafListener.engine;

@WebServlet(urlPatterns = "/", loadOnStartup = 1)
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10) //10 MB in memory limit
public class UploadServlet extends HttpServlet {

    private final UserProcessor userProcessor = new UserProcessor();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());

        engine.process("upload", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        UserDao dao = DBIProvider.getDao(UserDao.class);

        int count = Integer.parseInt(req.getParameter("count"));
        try {
//            http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html
            Part filePart = req.getPart("fileToUpload");
            if (filePart.getSize() == 0) {
                throw new IllegalStateException("Upload file have not been selected");
            }
            try (InputStream is = filePart.getInputStream()) {
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

                            if (users.size() == count){
                                executor.submit(() -> {

                                    dao.insertBatch(ImmutableList.copyOf(users).iterator(), count);
                                   return null;
                                });

                                users.clear();
                            }
                        }

                    }
                } catch (XMLStreamException e){

                }


                executor.shutdown();
              //  webContext.setVariable("users", conflictUsers);
                engine.process("result", webContext, resp.getWriter());
            }
        } catch (Exception e) {
            webContext.setVariable("exception", e);
            engine.process("exception", webContext, resp.getWriter());
        }
    }
}
