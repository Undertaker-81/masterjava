package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.persist.DBIProvider;

import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.type.GroupType;
import ru.javaops.masterjava.xml.schema.Project;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Panfilov Dmitriy
 * 17.02.2021
 */
@Slf4j
public class ProjectProcessor {
    private final GroupDao groupDao = DBIProvider.getDao(GroupDao.class);
    private final ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);

    public void process(StaxStreamProcessor processor) throws XMLStreamException, JAXBException {
        JaxbParser parser = new JaxbParser(Project.class);
        Project project;
        List<Project> projects = new ArrayList<>();
        while (processor.startElement("Project", "Projects")) {
            if ("Project".equals(processor.getReader().getLocalName())){
                project = parser.createUnmarshaller().unmarshal(processor.getReader(),Project.class);
                projects.add(project);
            }

        }
        Map<String, ru.javaops.masterjava.persist.model.Project> map = projectDao.getAsMap();
        projects.forEach(proj -> {
           if (!map.containsKey(proj.getName())){
               int projectId = projectDao.insertGeneratedId(new ru.javaops.masterjava.persist.model.Project(proj.getName(), proj.getDescription()));
               proj.getGroup()
                       .forEach(group -> groupDao.insert(new Group(group.getName(), GroupType.valueOf(group.getType().value()), projectId)));
           }else{
               proj.getGroup()
                       .forEach(group -> groupDao.insert(new Group(group.getName(), GroupType.valueOf(group.getType().value()), map.get(proj.getName()).getId())));
           }

        });


    }
}
