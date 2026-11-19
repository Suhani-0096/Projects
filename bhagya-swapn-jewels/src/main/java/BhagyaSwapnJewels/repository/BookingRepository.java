package BhagyaSwapnJewels.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import BhagyaSwapnJewels.entity.Booking;
import BhagyaSwapnJewels.entity.BookingStatus;

import jakarta.persistence.LockModeType;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Lock booking while approving or rejecting
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Booking b WHERE b.id = :id")
    Optional<Booking> findByIdForUpdate(@Param("id") Long id);

    // Find pending bookings older than the specified time
    // that have not received an owner reminder
    List<Booking> findByStatusAndCreatedAtBeforeAndOwnerReminderSentFalse(
            BookingStatus status,
            LocalDateTime time
    );
}
