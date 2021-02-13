package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.User;

import java.util.List;
import java.util.Map;

/**
 * @author Panfilov Dmitriy
 * 11.02.2021
 */
public class CityTestData {
    public static City spb;
    public static City mov;
    public static City kiv;
    public static City mnsk;

    public static List<City> CITIES;
    public static List<City> CITIES_WITH_ID;


    public static void init() {
        spb = new City("Санкт-Петербург", "spb");
        mov = new City("Москва", "mov");
        kiv = new City("Киев", "kiv");
        mnsk = new City("Минск", "mnsk");
        CITIES = ImmutableList.of(spb,mov,kiv,mnsk);
    }
    public static void setUp() {

        CityDao dao = DBIProvider.getDao(CityDao.class);

        dao.clean();
        DBIProvider.getDBI().useTransaction((handle) -> {
            CITIES.forEach(dao::insert);

        });


        CITIES_WITH_ID = dao.getTowns();
    }
}
