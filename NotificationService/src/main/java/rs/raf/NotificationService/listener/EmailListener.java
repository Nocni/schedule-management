package rs.raf.NotificationService.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import rs.raf.NotificationService.listener.helper.MessageHelper;
import rs.raf.NotificationService.services.NotificationService;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class EmailListener {

    private MessageHelper messageHelper;
    private NotificationService notificationService;

    public EmailListener(MessageHelper messageHelper, NotificationService notificationService) {
        this.messageHelper = messageHelper;
        this.notificationService = notificationService;
    }

    @JmsListener(destination = "${destination.sendEmails}", concurrency = "5-10")
    public void addOrder(Message message) throws JMSException {
        notificationService.sendSimpleMessage("nikolajr93og@gmail.com", "subject");
    }
}
