package ru.javaops.masterjava.persist.dao;

import org.junit.Before;
import org.junit.Test;
import ru.javaops.masterjava.persist.GroupTestData;
import ru.javaops.masterjava.persist.model.Group;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Panfilov Dmitriy
 * 12.02.2021
 */
public class GroupDaoTest extends AbstractDaoTest<GroupDao>{

    public GroupDaoTest() {
        super(GroupDao.class);
    }

    @Before
    public void setUp() throws Exception {
        GroupTestData.init();
        GroupTestData.setUp();

    }

    @Test
    public void insert() {
        GroupTestData.GROUPS.forEach(dao::insert);
    }

    @Test
    public void insertGeneratedId() {
    }

    @Test
    public void insertWitId() {
    }

    @Test
    public void getGroups() {
       List<Group> list = dao.getGroups();
        System.out.println(list);
    }

    @Test
    public void clean() {
        dao.clean();
    }
}