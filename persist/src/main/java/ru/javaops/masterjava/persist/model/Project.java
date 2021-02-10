package ru.javaops.masterjava.persist.model;

import lombok.*;

import java.util.List;

/**
 * @author Panfilov Dmitriy
 * 10.02.2021
 */
@NoArgsConstructor
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Project extends BaseEntity{
    private @NonNull String name;
    private @NonNull List<Group> groups;

    public Project(Integer id, String name, List<Group> groups){
        this(name, groups);
        this.id = id;
    }


}
