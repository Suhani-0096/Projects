package BhagyaSwapnJewels.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import BhagyaSwapnJewels.dto.CreateBookingRequest;
import BhagyaSwapnJewels.entity.Booking;
import BhagyaSwapnJewels.repository.BookingRepository;
import BhagyaSwapnJewels.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    public BookingController(
            BookingService bookingService,
            BookingRepository bookingRepository) {
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
    }

    // Customers can submit a booking without logging in.
    @PostMapping
    public ResponseEntity<?> createBooking(
            @RequestBody CreateBookingRequest request) {
        try {
            Booking booking = bookingService.createBooking(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(booking);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("message", ex.getMessage()));
        } catch (ArithmeticException ex) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("message", "Invalid product quantities."));
        }
    }

    // Only the owner can see all customer bookings.
    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Only the owner can approve a booking.
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> approveBooking(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookingService.approveBooking(id));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("message", ex.getMessage()));
        }
    }

    // Only the owner can reject a booking.
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> rejectBooking(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookingService.rejectBooking(id));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("message", ex.getMessage()));
        }
    }
}
