package ru.javaops.masterjava.upload;

import ru.javaops.masterjava.persist.DBIProvider;

import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;

/**
 * @author Panfilov Dmitriy
 * 17.02.2021
 */
public class ProjectProcessor {
    private final GroupDao groupDao = DBIProvider.getDao(GroupDao.class);
    private final ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);


}
