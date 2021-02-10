package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

/**
 * @author Panfilov Dmitriy
 * 10.02.2021
 */
@NoArgsConstructor
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Town extends BaseEntity{
    @Column("full_name")
    private @NonNull String fullName;
    @Column("short_name")
    private @NonNull String shortName;

    public Town(Integer id, String fullName, String shortName){
        this(fullName,shortName);
        this.id = id;
    }
}
