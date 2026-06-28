package com.utkarsh.booking_service.controller;

import com.utkarsh.booking_service.entity.Booking;
import com.utkarsh.booking_service.entity.Seat;
import com.utkarsh.booking_service.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // GET /api/bookings/seats/5
    @GetMapping("/seats/{showId}")
    public ResponseEntity<List<Seat>> getAvailableSeats(@PathVariable Long showId) {
        return ResponseEntity.ok(bookingService.getAvailableSeats(showId));
    }

    // POST /api/bookings/lock?showId=5&seatNumber=A1&userId=101
    @PostMapping("/lock")
    public ResponseEntity<String> lockSeat(
            @RequestParam Long showId,
            @RequestParam String seatNumber,
            @RequestParam Long userId) {
        return ResponseEntity.ok(bookingService.lockSeat(showId, seatNumber, userId));
    }

    // POST /api/bookings/confirm
    @PostMapping("/confirm")
    public ResponseEntity<Booking> confirmBooking(@RequestBody ConfirmRequest request) {
        Booking booking = bookingService.confirmBooking(
                request.getUserId(),
                request.getShowId(),
                request.getSeats(),
                request.getTotalAmount()
        );
        return ResponseEntity.ok(booking);
    }

    // GET /api/bookings/user/101
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }

    // PUT /api/bookings/cancel/1
    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }

    // Inner class - request body ke liye
    @lombok.Data
    static class ConfirmRequest {
        private Long userId;
        private Long showId;
        private List<String> seats;
        private double totalAmount;
    }
}