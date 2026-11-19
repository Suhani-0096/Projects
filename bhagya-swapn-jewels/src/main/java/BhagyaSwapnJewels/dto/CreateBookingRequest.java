package BhagyaSwapnJewels.dto;

import java.util.List;

public record CreateBookingRequest(
        String customerName,
        String mobile,
        String address,
        String pinCode,
        List<ItemRequest> items
        ) {

    public record ItemRequest(
            Long productId,
            Integer quantity
            ) {

    }
}
