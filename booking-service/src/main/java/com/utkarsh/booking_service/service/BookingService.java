package com.utkarsh.booking_service.service;

import com.utkarsh.booking_service.entity.Booking;
import com.utkarsh.booking_service.entity.Booking.BookingStatus;
import com.utkarsh.booking_service.entity.Seat;
import com.utkarsh.booking_service.kafka.BookingEventProducer;
import com.utkarsh.booking_service.repository.BookingRepository;
import com.utkarsh.booking_service.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final BookingEventProducer eventProducer;

    public List<Seat> getAvailableSeats(Long showId) {
        return seatRepository.findByShowIdAndIsBooked(showId, false);
    }

    public String lockSeat(Long showId, String seatNumber, Long userId) {

        String redisKey = "seat:" + showId + ":" + seatNumber;

        Boolean alreadyLocked = redisTemplate.hasKey(redisKey);

        if (Boolean.TRUE.equals(alreadyLocked)) {
            return "SEAT_ALREADY_LOCKED";
        }

        Seat seat = seatRepository
                .findByShowIdAndSeatNumber(showId, seatNumber)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        if (seat.isBooked()) {
            return "SEAT_ALREADY_BOOKED";
        }

        redisTemplate.opsForValue().set(
                redisKey,
                "LOCKED_BY_" + userId,
                10,
                TimeUnit.MINUTES
        );

        return "SEAT_LOCKED_SUCCESS";
    }

    public Booking confirmBooking(Long userId, Long showId,
                                  List<String> seats, double totalAmount) {

        for (String seatNumber : seats) {
            String redisKey = "seat:" + showId + ":" + seatNumber;
            String lockValue = redisTemplate.opsForValue().get(redisKey);

            // check karo lock is user ka hai
            if (lockValue == null || !lockValue.equals("LOCKED_BY_" + userId)) {
                throw new RuntimeException("Seat " + seatNumber + " lock expired or invalid!");
            }
        }

        Booking booking = Booking.builder()
                .userId(userId)
                .showId(showId)
                .selectedSeats(String.join(",", seats))
                .totalAmount(totalAmount)
                .status(BookingStatus.PENDING)
                .bookedAt(LocalDateTime.now())
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        for (String seatNumber : seats) {
            Seat seat = seatRepository
                    .findByShowIdAndSeatNumber(showId, seatNumber)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            seat.setBooked(true);
            seatRepository.save(seat);

            redisTemplate.delete("seat:" + showId + ":" + seatNumber);
        }

        eventProducer.sendBookingEvent(savedBooking);

        return savedBooking;
    }

    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        String[] seats = booking.getSelectedSeats().split(",");
        for (String seatNumber : seats) {
            Seat seat = seatRepository
                    .findByShowIdAndSeatNumber(booking.getShowId(), seatNumber)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            seat.setBooked(false);
            seatRepository.save(seat);
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
}