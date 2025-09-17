package rs.raf.RasporedService.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationMessageSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Value("${destination.sendEmails}")
    private String emailDestination;

    public NotificationMessageSender(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendScheduleChangeNotification(Long userId, String email, String message, String type) {
        try {
            ScheduleChangeMessage scheduleMessage = new ScheduleChangeMessage();
            scheduleMessage.setUserId(userId);
            scheduleMessage.setEmail(email);
            scheduleMessage.setMessage(message);
            scheduleMessage.setType(type);

            String jsonMessage = objectMapper.writeValueAsString(scheduleMessage);
            jmsTemplate.convertAndSend(emailDestination, jsonMessage);
            
            System.out.println("Notification sent: " + type + " to " + email);
        } catch (JsonProcessingException e) {
            System.err.println("Error sending notification: " + e.getMessage());
        }
    }

    // Inner class for the message structure
    public static class ScheduleChangeMessage {
        private Long userId;
        private String email;
        private String message;
        private String type;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }
}