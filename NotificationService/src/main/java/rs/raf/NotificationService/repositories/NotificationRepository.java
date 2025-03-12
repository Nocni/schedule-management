package rs.raf.NotificationService.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.raf.NotificationService.models.Notification;

public interface NotificationRepository extends CrudRepository<Notification, Integer> {
}
