package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.Project;


import java.util.List;

/**
 * @author Dmitriy Panfilov
 * 11.02.2021
 */
@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class ProjectDao implements AbstractDao{

    public Project insert(Project project) {
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
