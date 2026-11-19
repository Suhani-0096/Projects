package BhagyaSwapnJewels.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import BhagyaSwapnJewels.entity.Notification;
import BhagyaSwapnJewels.repository.NotificationRepository;

@RestController
@RequestMapping("/api/notifications")
@PreAuthorize("hasRole('OWNER')")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(
            NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // Get latest 50 notifications
    @GetMapping
    public List<Notification> getNotifications() {
        return notificationRepository
                .findTop50ByOrderByCreatedAtDesc();
    }

    // Get unread notification count
    @GetMapping("/unread-count")
    public Map<String, Long> getUnreadCount() {
        return Map.of(
                "count",
                notificationRepository.countByIsReadFalse()
        );
    }

    // Mark one notification as read
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {

        return notificationRepository.findById(id)
                .map(notification -> {
                    notification.setRead(true);
                    notificationRepository.save(notification);

                    return ResponseEntity.ok(
                            Map.of("message", "Notification marked as read")
                    );
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Mark all notifications as read
    @PutMapping("/read-all")
    @Transactional
    public Map<String, Object> markAllAsRead() {

        int updatedCount = notificationRepository.markAllAsRead();

        return Map.of(
                "message", "All notifications marked as read",
                "updatedCount", updatedCount
        );
    }
}
