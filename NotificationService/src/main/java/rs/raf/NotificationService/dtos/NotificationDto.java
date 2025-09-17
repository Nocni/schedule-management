package rs.raf.NotificationService.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationDto {
    
    private Long notificationId;
    private String message;
    private String email;
    private Long userId;
    private String type;
    private LocalDateTime time;
    
}