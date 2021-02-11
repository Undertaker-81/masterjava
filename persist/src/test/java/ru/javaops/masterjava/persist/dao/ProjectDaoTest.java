package ru.javaops.masterjava.persist.dao;

import org.junit.Before;
import org.junit.Test;
import ru.javaops.masterjava.persist.ProjectTestData;

import static org.junit.Assert.*;

/**
 * @author Dmitriy Panfilov
 * 11.02.2021
 */
public class ProjectDaoTest extends AbstractDaoTest<ProjectDao>{

    public ProjectDaoTest() {
        super(ProjectDao.class);
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void insert() {
        dao.insert(ProjectTestData.MASTERJAVA);
    }

    @Test
    public void getProjects() {
    }

    @Test
    public void clean() {
    }
}