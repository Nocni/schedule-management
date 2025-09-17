package rs.raf.NotificationService.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import rs.raf.NotificationService.dtos.ScheduleChangeMessageDto;
import rs.raf.NotificationService.listener.helper.MessageHelper;
import rs.raf.NotificationService.services.NotificationService;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class EmailListener {

    private final MessageHelper messageHelper;
    private final NotificationService notificationService;

    public EmailListener(MessageHelper messageHelper, NotificationService notificationService) {
        this.messageHelper = messageHelper;
        this.notificationService = notificationService;
    }

    @JmsListener(destination = "${destination.sendEmails}", concurrency = "5-10")
    public void handleScheduleChange(Message message) throws JMSException {
        try {
            ScheduleChangeMessageDto messageDto = messageHelper.getMessage(message, ScheduleChangeMessageDto.class);
            
            // Save notification to database
            notificationService.saveNotification(
                messageDto.getEmail(), 
                messageDto.getUserId(), 
                messageDto.getMessage(), 
                messageDto.getType()
            );
            
            // Send email notification
            String subject = "Raspored - " + messageDto.getType();
            notificationService.sendSimpleMessage(
                messageDto.getEmail(), 
                subject, 
                messageDto.getMessage()
            );
            
        } catch (Exception e) {
            System.err.println("Error processing schedule change message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
