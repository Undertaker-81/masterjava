package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
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
public class Group extends BaseEntity{
    private @NonNull String name;
    private @NonNull StatusGroup status;
    @Column("project_id")
    private @NonNull Integer projectId;


    public Group(Integer id, String name, StatusGroup status, Integer projectId){
        this(name, status, projectId);
        this.id = id;
    }

}
