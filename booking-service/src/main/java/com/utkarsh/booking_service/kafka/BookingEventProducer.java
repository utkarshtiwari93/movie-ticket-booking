package com.utkarsh.booking_service.kafka;

import com.utkarsh.booking_service.entity.Booking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEventProducer {

    private final KafkaTemplate<String, Booking> kafkaTemplate;

    private static final String TOPIC = "booking-events";

    public void sendBookingEvent(Booking booking) {
        log.info("Sending booking event for bookingId: {}", booking.getId());
        kafkaTemplate.send(TOPIC, String.valueOf(booking.getId()), booking);
        log.info("Booking event sent successfully!");
    }
}