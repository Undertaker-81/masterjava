package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;
import org.jdbi.v3.core.annotation.Unmappable;
import org.jdbi.v3.core.mapper.Nested;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.beans.ConstructorProperties;
import java.util.List;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class User extends BaseEntity {
    @ColumnName("full_name")
    private @NonNull String fullName;
    @ColumnName("email")
    private @NonNull String email;
    @ColumnName("flag")
    private @NonNull UserFlag flag;
    private @NonNull  City city;
    private List<Group> groups;

    public User(@ColumnName("id") Integer id, String fullName, String email, UserFlag flag, @Nested City city) {
        this(fullName, email, flag, city);
        this.id=id;
    }
    public int getCityId(){
        return city.id;
    }

}