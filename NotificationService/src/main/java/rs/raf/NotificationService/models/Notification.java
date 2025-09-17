package rs.raf.NotificationService.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private String message;

    private String email;
    
    private Long userId;

    private String type; // "DODAVANJE", "IZMENA", "BRISANJE"

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime time;
    
    @PrePersist
    protected void onCreate() {
        time = LocalDateTime.now();
    }

}
