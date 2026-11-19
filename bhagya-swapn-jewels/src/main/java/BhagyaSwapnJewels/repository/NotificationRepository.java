package BhagyaSwapnJewels.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import BhagyaSwapnJewels.entity.Notification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;



public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.isRead = false")
    int markAllAsRead();

    // Get the latest 50 notifications
    List<Notification> findTop50ByOrderByCreatedAtDesc();

    // Count unread notifications
    long countByIsReadFalse();

    // Get all unread notifications
    List<Notification> findByIsReadFalse();
}
