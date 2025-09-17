package rs.raf.NotificationService.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import rs.raf.NotificationService.dtos.NotificationCreateDto;
import rs.raf.NotificationService.dtos.NotificationDto;
import rs.raf.NotificationService.mapper.NotificationMapper;
import rs.raf.NotificationService.models.Notification;
import rs.raf.NotificationService.repositories.NotificationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationService(JavaMailSender mailSender, 
                             NotificationRepository notificationRepository, 
                             NotificationMapper notificationMapper) {
        this.mailSender = mailSender;
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public NotificationDto createNotification(Long userId, String email, NotificationCreateDto createDto) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setEmail(email);
        notification.setMessage(createDto.getMessage());
        notification.setType(createDto.getType());
        
        Notification saved = notificationRepository.save(notification);
        
        // Send email notification
        String subject = "Raspored - " + createDto.getType();
        sendSimpleMessage(email, subject, createDto.getMessage());
        
        return notificationMapper.notificationToDto(saved);
    }

    public List<NotificationDto> getNotificationsByUserId(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByTimeDesc(userId);
        return notifications.stream()
                .map(notificationMapper::notificationToDto)
                .collect(Collectors.toList());
    }

    public List<NotificationDto> getNotificationsByEmail(String email) {
        List<Notification> notifications = notificationRepository.findByEmailOrderByTimeDesc(email);
        return notifications.stream()
                .map(notificationMapper::notificationToDto)
                .collect(Collectors.toList());
    }

    public void saveNotification(String email, Long userId, String message, String type) {
        Notification notification = new Notification();
        notification.setEmail(email);
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setType(type);
        
        notificationRepository.save(notification);
    }

}
