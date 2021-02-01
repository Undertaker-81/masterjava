package ru.javaops.masterjava.web;

import com.google.common.io.Resources;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.web.config.TemplateEngineUtil;
import ru.javaops.masterjava.xml.schema.FlagType;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Dmitriy Panfilov
 * 31.01.2021
 */
@WebServlet("/upload")
@MultipartConfig(location = "/tmp")
public class UploadServlet extends HttpServlet {
    private final Set<User> users = new HashSet<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
            WebContext context = new WebContext(request, response, request.getServletContext());
              context.setVariable("users", users);

            engine.process("upload.html", context, response.getWriter());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (isMultipart){
            // Create a factory for disk-based file items
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // Configure a repository (to ensure a secure temp location is used)
            ServletContext servletContext = this.getServletConfig().getServletContext();
            File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            factory.setRepository(repository);

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            // Parse the request

            try {
                List<FileItem> items = upload.parseRequest(req);
                for (FileItem file : items){
                    try (StaxStreamProcessor processor =
                                 new StaxStreamProcessor(file.getInputStream())) {
                        XMLStreamReader reader = processor.getReader();
                        while (reader.hasNext()) {
                            int event = reader.next();
                            if (event == XMLEvent.START_ELEMENT) {
                                if ("User".equals(reader.getLocalName())) {
                                    User user = new User();

                                    user.setFlag(FlagType.fromValue(reader.getAttributeValue(0)));
                                  //  String flag = reader.getAttributeValue(0);
                                  user.setEmail(reader.getAttributeValue(2));
                                  user.setValue(reader.getElementText());
                                  users.add(user);
                                }
                            }
                        }
                    }

                }
            } catch (FileUploadException | XMLStreamException e) {
                e.printStackTrace();
            }

            resp.sendRedirect("/upload");

        }
    }
}
