package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.persist.model.StatusGroup;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Panfilov Dmitriy
 * 12.02.2021
 */
public class GroupTestData {
    public static Group masterjava01;
    public static Group topjava06;
    public static Group topjava07;
    public static Group topjava08;
    public static List<Group> GROUPS;
    public static Map<String, Project> projectMap;

    public static void init() {
        ProjectTestData.setUp();
        projectMap = ProjectTestData.projects.stream().collect(Collectors.toMap(Project::getName, project -> project));
        masterjava01 = new Group("masterjava01", StatusGroup.CURRENT, projectMap.get("MasterJava"));
        topjava06 = new Group("topjava06", StatusGroup.FINISHED, projectMap.get("TopJava"));
        topjava07 = new Group("topjava07", StatusGroup.FINISHED, projectMap.get("TopJava"));
        topjava08 = new Group("topjava08", StatusGroup.CURRENT, projectMap.get("TopJava"));
        GROUPS = ImmutableList.of(masterjava01, topjava06, topjava07, topjava08);

    }
    public static void setUp() {
        GroupDao dao = DBIProvider.getDao(GroupDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((handle) -> {
            GROUPS.forEach(dao::insert);

        });

    }
}
