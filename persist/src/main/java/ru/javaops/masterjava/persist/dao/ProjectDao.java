package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.Project;


import java.util.List;

/**
 * @author Dmitriy Panfilov
 * 11.02.2021
 */
//@RegisterMapperFactory(EntityMapperFactory.class)
@RegisterBeanMapper(Project.class)
public interface ProjectDao extends AbstractDao{

    default Project insert(Project project) {
        if (project.isNew()) {
            int id = insertGeneratedId(project);
            project.setId(id);
        } else {
            insertWitId(project);
        }
        return project;
    }

    @SqlUpdate("INSERT INTO projects (name) VALUES (:name) ON CONFLICT DO NOTHING")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean Project project);

    @SqlUpdate("INSERT INTO projects (id, name) VALUES (:id, name) ON CONFLICT DO NOTHING")
    abstract void insertWitId(@BindBean Project project);

    @SqlQuery("SELECT * FROM projects")
    public abstract List<Project> getProjects();

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE projects CASCADE")
    @Override
    public abstract void clean();
}
