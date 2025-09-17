package rs.raf.NotificationService.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationCreateDto {
    
    private String message;
    private String type;
    
}