package BhagyaSwapnJewels.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import BhagyaSwapnJewels.dto.CreateBookingRequest;
import BhagyaSwapnJewels.entity.Booking;
import BhagyaSwapnJewels.entity.BookingItem;
import BhagyaSwapnJewels.entity.BookingStatus;
import BhagyaSwapnJewels.entity.Notification;
import BhagyaSwapnJewels.entity.Product;
import BhagyaSwapnJewels.repository.BookingRepository;  
import BhagyaSwapnJewels.repository.NotificationRepository;
import BhagyaSwapnJewels.repository.ProductRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ProductRepository productRepository;
    private final NotificationRepository notificationRepository;

    public BookingService(
            BookingRepository bookingRepository,
            ProductRepository productRepository,
            NotificationRepository notificationRepository) {

        this.bookingRepository = bookingRepository;
        this.productRepository = productRepository;
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public Booking createBooking(CreateBookingRequest request) {

        validateCustomer(request);

        // Combine repeated product IDs to avoid duplicate item lines.
        Map<Long, Integer> quantities = new HashMap<>();

        for (CreateBookingRequest.ItemRequest item : request.items()) {
            if (item == null || item.productId() == null
                    || item.quantity() == null || item.quantity() <= 0) {
                throw new IllegalArgumentException(
                        "Each item must have a product ID and a positive quantity.");
            }

            quantities.merge(item.productId(), item.quantity(), Math::addExact);
        }

        Booking booking = new Booking();
        booking.setCustomerName(request.customerName().trim());
        booking.setMobile(request.mobile().trim());
        booking.setAddress(request.address().trim());
        booking.setPinCode(request.pinCode().trim());
        booking.setStatus(BookingStatus.PENDING);

        // Lock product rows in sorted ID order to reduce deadlock risk.
        List<Long> productIds = new ArrayList<>(quantities.keySet());
        productIds.sort(Long::compareTo);

        for (Long productId : productIds) {
            Product product = productRepository.findByIdForUpdate(productId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Product not found: " + productId));

            int quantity = quantities.get(productId);

            if (!product.isActive()) {
                throw new IllegalArgumentException(
                        product.getName() + " is not available.");
            }

            if (product.getStockQuantity() == null
                    || product.getStockQuantity() < quantity) {
                throw new IllegalArgumentException(
                        "Insufficient stock for " + product.getName());
            }

            BookingItem bookingItem = new BookingItem();
            bookingItem.setProductId(product.getId());
            bookingItem.setProductName(product.getName());
            bookingItem.setUnitPrice(product.getPrice());
            bookingItem.setQuantity(quantity);

            booking.addItem(bookingItem);
        }

        // Stock is NOT reduced here.
        Booking savedBooking = bookingRepository.save(booking);

        createNotification(
                "New Booking Received",
                "A new booking #" + savedBooking.getId()
                + " has been submitted. Please review it.",
                "NEW_BOOKING",
                savedBooking.getId()
        );

        return savedBooking;
    }

    @Transactional
    public Booking approveBooking(Long bookingId) {

        Booking booking = bookingRepository.findByIdForUpdate(bookingId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Booking not found: " + bookingId));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending bookings can be approved.");
        }

        List<BookingItem> items = booking.getItems();

        // Lock every product in a consistent order.
        List<Long> productIds = items.stream()
                .map(BookingItem::getProductId)
                .distinct()
                .sorted()
                .toList();

        Map<Long, Product> lockedProducts = new HashMap<>();

        for (Long productId : productIds) {
            Product product = productRepository.findByIdForUpdate(productId)
                    .orElseThrow(() -> new IllegalStateException(
                            "Product no longer exists: " + productId));

            lockedProducts.put(productId, product);
        }

        // Validate ALL items before changing ANY stock.
        for (BookingItem item : items) {
            Product product = lockedProducts.get(item.getProductId());

            if (!product.isActive()
                    || product.getStockQuantity() == null
                    || product.getStockQuantity() < item.getQuantity()) {
                throw new IllegalStateException(
                        "Insufficient stock for " + item.getProductName()
                        + ". The booking remains pending.");
            }
        }

        // The transaction commits all changes together.
        for (BookingItem item : items) {
            Product product = lockedProducts.get(item.getProductId());
            product.setStockQuantity(
                    product.getStockQuantity() - item.getQuantity());
        }

        booking.setStatus(BookingStatus.APPROVED);

        return bookingRepository.save(booking);
    }

    private void createNotification(
            String title,
            String message,
            String type,
            Long bookingId) {

        Notification notification = new Notification();

        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setBookingId(bookingId);
        notification.setRead(false);

        notificationRepository.save(notification);
    }

    @Transactional
    public Booking rejectBooking(Long bookingId) {

        Booking booking = bookingRepository.findByIdForUpdate(bookingId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Booking not found: " + bookingId));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending bookings can be rejected.");
        }

        booking.setStatus(BookingStatus.REJECTED);

        // Rejection never changes product stock.
        return bookingRepository.save(booking);
    }

    private void validateCustomer(CreateBookingRequest request) {

        if (request == null
                || request.customerName() == null
                || request.customerName().isBlank()
                || request.customerName().length() > 100
                || request.mobile() == null
                || !request.mobile().trim().matches("[0-9]{10}")
                || request.address() == null
                || request.address().isBlank()
                || request.address().length() > 500
                || request.pinCode() == null
                || !request.pinCode().trim().matches("[0-9]{6}")
                || request.items() == null
                || request.items().isEmpty()) {
            throw new IllegalArgumentException(
                    "Please provide a valid name, 10-digit mobile number, "
                    + "address, 6-digit PIN code, and at least one product.");
        }
    }
}
