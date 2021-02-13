package ru.javaops.masterjava.persist.dao;


import one.util.streamex.IntStreamEx;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.generic.GenericType;
import org.jdbi.v3.core.mapper.JoinRow;
import org.jdbi.v3.core.mapper.JoinRowMapper;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterJoinRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.*;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import ru.javaops.masterjava.persist.DBIProvider;

import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@RegisterMapperFactory(EntityMapperFactory.class)
@RegisterBeanMapper(User.class)
public interface UserDao extends AbstractDao {

    default User insert(User user) {
        if (user.isNew()) {
            int id = insertGeneratedId(user);
            user.setId(id);
        } else {
            insertWitId(user);
        }
        return user;
    }

    @SqlQuery("SELECT nextval('user_seq')")
    int getNextVal();

    @Transaction
     default int getSeqAndSkip(int step) {
        int id = getNextVal();
        DBIProvider.getDBI().useHandle(h -> h.execute("SELECT setval('user_seq', " + (id + step - 1) + ")"));
        return id;
    }

    @SqlUpdate("INSERT INTO users (full_name, email, flag, town_id) VALUES (:fullName, :email, CAST(:flag AS USER_FLAG), :cityId) ")
    @GetGeneratedKeys
     int insertGeneratedId(@BindBean User user);



    @SqlUpdate("INSERT INTO users (id, full_name, email, flag, town_id) VALUES (:id, :fullName, :email, CAST(:flag AS USER_FLAG), :cityId) ")
     void insertWitId(@BindBean User user);

    @SqlQuery("SELECT * FROM users ORDER BY full_name, email LIMIT ?")
      List<User> getWithLimit(@Bind int limit);

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE users CASCADE")
    @Override
      void clean();

    //    https://habrahabr.ru/post/264281/
    @SqlBatch("INSERT INTO users (id, full_name, email, flag, town_id) VALUES (:id, :fullName, :email, CAST(:flag AS USER_FLAG), :cityId)" +
            "ON CONFLICT DO NOTHING")
//            "ON CONFLICT (email) DO UPDATE SET full_name=:fullName, flag=CAST(:flag AS USER_FLAG)")
      int[] insertBatch(@BindBean List<User> users, @BatchChunkSize int chunkSize);


     default List<String> insertAndGetConflictEmails(List<User> users) {
        int[] result = insertBatch(users, users.size());
        return IntStreamEx.range(0, users.size())
                .filter(i -> result[i] == 0)
                .mapToObj(index -> users.get(index).getEmail())
                .toList();
    }

    default List<User> getUsers(){
        Handle handle = DBIProvider.getDBI().open();

        String sql = "select u.id u_id, u.full_name u_full_name, u.email u_email , u.flag u_flag , " +
                " t.id t_id, t.short_name t_short_name, t.full_name t_full_name" +
                " from users u inner join  towns t on u.town_id = t.id";

       List<User> list = new ArrayList<>(handle
               .createQuery(sql)
               .registerRowMapper(BeanMapper.factory(User.class, "u"))
               .registerRowMapper(BeanMapper.factory(City.class, "t"))
               .reduceRows(new LinkedHashMap<Long, User>(),
                       (map, rowView) -> {
                           User user = map.computeIfAbsent(
                                   rowView.getColumn("u_id", Long.class),
                                   id -> rowView.getRow(User.class));

                           if (rowView.getColumn("t_id", Long.class) != null) {
                               user.setCity(rowView.getRow(City.class));
                           }

                           return map;
                       })
               .values());
      return list;
    }
    @SqlQuery("select u.id as u_id, u.full_name as u_full_name, u.email as u_email , u.flag as u_flag , " +
            " t.id as t_id,  t.short_name as t_short_name, t.full_name as t_full_name" +
            " from users u inner join  towns t on u.town_id = t.id")
    @RegisterJoinRowMapper({User.class, City.class})
    List<JoinRow> getUsers2();
}
