package com.utkarsh.booking_service.repository;

import com.utkarsh.booking_service.entity.Booking;
import com.utkarsh.booking_service.entity.Booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByShowId(Long showId);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);
}