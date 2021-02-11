package ru.javaops.masterjava.persist.dao;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.CityTestData;
import ru.javaops.masterjava.persist.UserTestData;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Panfilov Dmitriy
 * 11.02.2021
 */
public class CityDaoTest extends AbstractDaoTest<CityDao>{

    public CityDaoTest() {
        super(CityDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        CityTestData.init();
    }
    @Before
    public void setUp() throws Exception {
        CityTestData.setUp();
    }



    @Test
    public void insertGeneratedId() {
    }

    @Test
    public void insertWitId() {
    }

    @Test
    public void getTowns() {
        List<City> cityList = dao.getTowns();
        assertEquals(CityTestData.CITIES, cityList);
    }

    @Test
    public void clean() {
        dao.clean();
    }
}