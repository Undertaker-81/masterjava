package ru.javaops.masterjava.web.config;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import static ru.javaops.masterjava.web.config.TemplateEngineUtil.*;
/**
 * @author Dmitriy Panfilov
 * 31.01.2021
 */
@WebListener
public class ThymeleafConfig  implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        TemplateEngine engine = templateEngine(servletContextEvent.getServletContext());
        storeTemplateEngine(servletContextEvent.getServletContext(), engine);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
    private TemplateEngine templateEngine(ServletContext servletContext) {
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(templateResolver(servletContext));
        return engine;
    }

    private ITemplateResolver templateResolver(ServletContext servletContext) {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(servletContext);
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }
}
