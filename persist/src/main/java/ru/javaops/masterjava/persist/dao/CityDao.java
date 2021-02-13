package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.javaops.masterjava.persist.model.City;


import java.util.List;
import java.util.Optional;

/**
 * @author Panfilov Dmitriy
 * 10.02.2021
 */
//@RegisterMapperFactory(EntityMapperFactory.class)
@RegisterBeanMapper(City.class)
public interface CityDao extends AbstractDao {

    default City insert(City city) {
        if (city.isNew()) {
            Integer id = insertGeneratedId(city);
            if (id == null){
                id = getId(city.getShortName());
            }
            city.setId(id);
        } else {
            insertWitId(city);
        }
        return city;
    }

    @SqlUpdate("INSERT INTO towns (id, full_name, short_name) VALUES (nextval('user_seq'), :fullName, :shortName) ON CONFLICT DO NOTHING")
    @GetGeneratedKeys("id")
    Integer insertGeneratedId(@BindBean City city);

    @SqlUpdate("INSERT INTO towns (id, full_name, short_name) VALUES (:id, :fullName, :shortName) ON CONFLICT DO NOTHING")
    void insertWitId(@BindBean City city);

    @SqlQuery("SELECT * FROM towns")
    List<City> getTowns();

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE towns CASCADE")
    @Override
    void clean();

    @SqlQuery("select id from towns where short_name = :shotName")
    int getId(@Bind("shotName") String shortName);
}
