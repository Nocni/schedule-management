package rs.raf.NotificationService.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import rs.raf.NotificationService.dtos.NotificationCreateDto;
import rs.raf.NotificationService.dtos.NotificationDto;
import rs.raf.NotificationService.services.NotificationService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSendNotification() throws Exception {
        // Arrange
        NotificationCreateDto createDto = new NotificationCreateDto();
        createDto.setMessage("Novi termin je dodat");
        createDto.setType("DODAVANJE");

        NotificationDto responseDto = new NotificationDto();
        responseDto.setNotificationId(1L);
        responseDto.setUserId(123L);
        responseDto.setEmail("test@example.com");
        responseDto.setMessage("Novi termin je dodat");
        responseDto.setType("DODAVANJE");
        responseDto.setTime(LocalDateTime.now());

        when(notificationService.createNotification(eq(123L), eq("test@example.com"), any(NotificationCreateDto.class)))
                .thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/notifikacije/123")
                .param("email", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.notificationId").value(1))
                .andExpect(jsonPath("$.userId").value(123))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.message").value("Novi termin je dodat"))
                .andExpect(jsonPath("$.type").value("DODAVANJE"));
    }

    @Test
    public void testGetNotificationsForUser() throws Exception {
        // Arrange
        NotificationDto notification1 = new NotificationDto();
        notification1.setNotificationId(1L);
        notification1.setUserId(123L);
        notification1.setMessage("Termin je dodat");
        notification1.setType("DODAVANJE");
        notification1.setTime(LocalDateTime.now());

        NotificationDto notification2 = new NotificationDto();
        notification2.setNotificationId(2L);
        notification2.setUserId(123L);
        notification2.setMessage("Termin je obrisan");
        notification2.setType("BRISANJE");
        notification2.setTime(LocalDateTime.now());

        List<NotificationDto> notifications = Arrays.asList(notification1, notification2);

        when(notificationService.getNotificationsByUserId(123L)).thenReturn(notifications);

        // Act & Assert
        mockMvc.perform(get("/notifikacije/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].notificationId").value(1))
                .andExpect(jsonPath("$[0].type").value("DODAVANJE"))
                .andExpect(jsonPath("$[1].notificationId").value(2))
                .andExpect(jsonPath("$[1].type").value("BRISANJE"));
    }

    @Test
    public void testGetNotificationsByEmail() throws Exception {
        // Arrange
        NotificationDto notification = new NotificationDto();
        notification.setNotificationId(1L);
        notification.setEmail("test@example.com");
        notification.setMessage("Termin je izmenjen");
        notification.setType("IZMENA");
        notification.setTime(LocalDateTime.now());

        List<NotificationDto> notifications = Arrays.asList(notification);

        when(notificationService.getNotificationsByEmail("test@example.com")).thenReturn(notifications);

        // Act & Assert
        mockMvc.perform(get("/notifikacije/email/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].type").value("IZMENA"));
    }
}