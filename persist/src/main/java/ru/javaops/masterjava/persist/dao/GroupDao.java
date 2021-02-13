package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.Group;



import java.util.List;

/**
 * @author Dmitriy Panfilov
 * 11.02.2021
 */
//@RegisterMapperFactory(EntityMapperFactory.class)
@RegisterBeanMapper(Group.class)
public interface  GroupDao extends AbstractDao{
    default Group insert(Group group) {
        if (group.isNew()) {
            int id = insertGeneratedId(group);
            group.setId(id);
        } else {
            insertWitId(group);
        }
        return group;
    }

    @SqlUpdate("INSERT INTO groups (name, status, project_id) VALUES (:name, CAST(:status AS group_status), :projectId) ON CONFLICT DO NOTHING")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean Group group);

    @SqlUpdate("INSERT INTO groups (id, name, status, project_id) VALUES (:id, :name, CAST(:status AS group_status), :projectId) ON CONFLICT DO NOTHING")
     void insertWitId(@BindBean Group group);

    @SqlQuery("SELECT * from groups")
     List<Group> getGroups();



    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE groups CASCADE")
    @Override
     void clean();
}
