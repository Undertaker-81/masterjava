package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class User extends BaseEntity {
    @Column("full_name")
    private @NonNull String fullName;
    private @NonNull String email;
    private @NonNull UserFlag flag;
    private @NonNull City city;
    private List<Group> groups;

    public User(Integer id, String fullName, String email, UserFlag flag, City city) {
        this(fullName, email, flag, city);
        this.id=id;
    }
    public int getCityId(){
        return city.id;
    }
}