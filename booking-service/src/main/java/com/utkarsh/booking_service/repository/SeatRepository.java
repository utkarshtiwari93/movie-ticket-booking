package com.utkarsh.booking_service.repository;

import com.utkarsh.booking_service.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByShowId(Long showId);

    List<Seat> findByShowIdAndIsBooked(Long showId, boolean isBooked);

    Optional<Seat> findByShowIdAndSeatNumber(Long showId, String seatNumber);
}