package ru.javaops.masterjava.upload;

import one.util.streamex.StreamEx;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.UserGroupDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserGroup;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.function.Function.identity;

/**
 * @author Panfilov Dmitriy
 * 18.02.2021
 */
public class GroupProcessor {
    private static final GroupDao groupDao = DBIProvider.getDao(GroupDao.class);
    private static final UserGroupDao userGroupDao = DBIProvider.getDao(UserGroupDao.class);
    private static final Map<String,Group> groups = StreamEx.of(groupDao.getAll()).toMap(Group::getName, identity());

    public boolean process(User user, List<String> userGroups){
        AtomicBoolean isGroupExistDB = new AtomicBoolean(true);
        List<UserGroup> userGroupList = new ArrayList<>();

       // Map<String,Group> groups = groupDao.getAll().stream().collect(Collectors.toMap(Group::getName, group -> group));
        userGroups.forEach(group -> {
            if (groups.containsKey(group)){
               userGroupList.add(new UserGroup(user.getId(), groups.get(group).getId()));
            }
            else{
                isGroupExistDB.set(false);
            }
        });
        userGroupDao.insertBatch(userGroupList);
        return isGroupExistDB.get();
    }
}
