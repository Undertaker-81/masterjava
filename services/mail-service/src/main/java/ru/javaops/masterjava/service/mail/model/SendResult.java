package ru.javaops.masterjava.service.mail.model;

import lombok.*;
import ru.javaops.masterjava.persist.model.BaseEntity;

import java.time.LocalDateTime;

/**
 * @author Dmitriy Panfilov
 * 16.02.2021
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@AllArgsConstructor
public class SendResult extends BaseEntity {
    private LocalDateTime dateTime;
    private String address;
    private boolean result;
}
