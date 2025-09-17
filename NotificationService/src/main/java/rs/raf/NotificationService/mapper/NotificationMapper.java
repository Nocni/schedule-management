package rs.raf.NotificationService.mapper;

import org.springframework.stereotype.Component;
import rs.raf.NotificationService.dtos.NotificationDto;
import rs.raf.NotificationService.models.Notification;

@Component
public class NotificationMapper {
    
    public NotificationDto notificationToDto(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setNotificationId(notification.getNotificationId());
        dto.setMessage(notification.getMessage());
        dto.setEmail(notification.getEmail());
        dto.setUserId(notification.getUserId());
        dto.setType(notification.getType());
        dto.setTime(notification.getTime());
        return dto;
    }
    
}