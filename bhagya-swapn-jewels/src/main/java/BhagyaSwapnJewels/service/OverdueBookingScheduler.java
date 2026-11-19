package BhagyaSwapnJewels.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import BhagyaSwapnJewels.entity.Booking;
import BhagyaSwapnJewels.entity.BookingStatus;
import BhagyaSwapnJewels.entity.Notification;
import BhagyaSwapnJewels.repository.BookingRepository;
import BhagyaSwapnJewels.repository.NotificationRepository;

@Component
public class OverdueBookingScheduler {

    private final BookingRepository bookingRepository;
    private final NotificationRepository notificationRepository;

    public OverdueBookingScheduler(
            BookingRepository bookingRepository,
            NotificationRepository notificationRepository) {

        this.bookingRepository = bookingRepository;
        this.notificationRepository = notificationRepository;
    }

    // Runs every minute
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkOverdueBookings() {

        LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);

        List<Booking> overdueBookings
                = bookingRepository
                        .findByStatusAndCreatedAtBeforeAndOwnerReminderSentFalse(
                                BookingStatus.PENDING,
                                twoHoursAgo
                        );

        for (Booking booking : overdueBookings) {

            booking.setOverdue(true);
            booking.setOwnerReminderSent(true);

            Notification notification = new Notification();

            notification.setTitle("Booking Overdue!");
            notification.setMessage(
                    "Booking #" + booking.getId()
                    + " has been pending for more than 2 hours. "
                    + "Please review it."
            );

            notification.setType("OVERDUE_BOOKING");
            notification.setBookingId(booking.getId());
            notification.setRead(false);

            notificationRepository.save(notification);
        }

        bookingRepository.saveAll(overdueBookings);
    }
}
