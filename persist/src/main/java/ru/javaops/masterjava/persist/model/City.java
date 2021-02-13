package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

/**
 * @author Panfilov Dmitriy
 * 10.02.2021
 */
@NoArgsConstructor
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class City extends BaseEntity{
    @ColumnName("full_name")
    private @NonNull String fullName;
    @ColumnName("short_name")
    private @NonNull String shortName;

    public City(Integer id, String fullName, String shortName){
        this.fullName = fullName;
        this.shortName = shortName;
        this.id = id;
    }
}
