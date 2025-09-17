package rs.raf.NotificationService.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.NotificationService.dtos.NotificationCreateDto;
import rs.raf.NotificationService.dtos.NotificationDto;
import rs.raf.NotificationService.services.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/notifikacije")
@CrossOrigin
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/{korisnickiId}")
    public ResponseEntity<NotificationDto> sendNotification(
            @PathVariable Long korisnickiId,
            @RequestParam String email,
            @RequestBody NotificationCreateDto createDto) {
        
        try {
            NotificationDto notification = notificationService.createNotification(korisnickiId, email, createDto);
            return new ResponseEntity<>(notification, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{korisnickiId}")
    public ResponseEntity<List<NotificationDto>> getNotificationsForUser(@PathVariable Long korisnickiId) {
        try {
            List<NotificationDto> notifications = notificationService.getNotificationsByUserId(korisnickiId);
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<NotificationDto>> getNotificationsByEmail(@PathVariable String email) {
        try {
            List<NotificationDto> notifications = notificationService.getNotificationsByEmail(email);
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
