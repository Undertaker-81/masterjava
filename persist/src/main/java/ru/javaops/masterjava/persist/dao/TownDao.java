package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.Town;



import java.util.List;

/**
 * @author Panfilov Dmitriy
 * 10.02.2021
 */
@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class TownDao implements AbstractDao{

    public Town insert(Town town) {
        if (town.isNew()) {
            int id = insertGeneratedId(town);
            town.setId(id);
        } else {
            insertWitId(town);
        }
        return town;
    }

    @SqlUpdate("INSERT INTO towns (full_name, short_name) VALUES (:fullName, :shortName) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean Town town);

    @SqlUpdate("INSERT INTO towns (id, full_name, short_name) VALUES (:id, :fullName, :shortName) ")
    abstract void insertWitId(@BindBean Town town);

    @SqlQuery("SELECT * FROM towns ORDER BY full_name LIMIT :it")
    public abstract List<Town> getWithLimit(@Bind int limit);

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE towns")
    @Override
    public abstract void clean();
}
