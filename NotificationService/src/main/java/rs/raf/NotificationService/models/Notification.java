package rs.raf.NotificationService.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Getter
@Setter
@Entity
public class Notification {

    @Id
    private long notificationId;

    private String message;

    private String email;

    private String type;

    @LastModifiedDate
    private Instant time;

}
