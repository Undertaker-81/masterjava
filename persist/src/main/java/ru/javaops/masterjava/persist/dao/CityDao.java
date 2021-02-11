package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.City;


import java.util.List;

/**
 * @author Panfilov Dmitriy
 * 10.02.2021
 */
@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao{

    public City insert(City city) {
        if (city.isNew()) {
            int id = insertGeneratedId(city);
            city.setId(id);
        } else {
            insertWitId(city);
        }
        return city;
    }

    @SqlUpdate("INSERT INTO towns (full_name, short_name) VALUES (:fullName, :shortName) ON CONFLICT DO NOTHING")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean City city);

    @SqlUpdate("INSERT INTO towns (id, full_name, short_name) VALUES (:id, :fullName, :shortName) ON CONFLICT DO NOTHING")
    abstract void insertWitId(@BindBean City city);

    @SqlQuery("SELECT * FROM towns")
    public abstract List<City> getTowns();

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE towns CASCADE")
    @Override
    public abstract void clean();


}
