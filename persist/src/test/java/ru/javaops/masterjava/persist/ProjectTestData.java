package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;

/**
 * @author Dmitriy Panfilov
 * 11.02.2021
 */
public class ProjectTestData {
    public static Project MASTERJAVA = new Project("MasterJava");
    public static Project TOPJAVA = new Project("TopJava");
    public static List<Project> projects ;

    public static void setUp() {
        ProjectDao dao = DBIProvider.getDao(ProjectDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {

            dao.insert(MASTERJAVA);
            dao.insert(TOPJAVA);
        });
        projects = dao.getProjects();
    }

}
