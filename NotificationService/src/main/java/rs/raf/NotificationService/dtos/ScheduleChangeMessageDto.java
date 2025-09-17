package rs.raf.NotificationService.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleChangeMessageDto {
    
    private Long userId;
    private String email;
    private String message;
    private String type; // DODAVANJE, IZMENA, BRISANJE
    
}