package rs.raf.NotificationService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.NotificationService.models.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByUserIdOrderByTimeDesc(Long userId);
    
    List<Notification> findByEmailOrderByTimeDesc(String email);
    
}
