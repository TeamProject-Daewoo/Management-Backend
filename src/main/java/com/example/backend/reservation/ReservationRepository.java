package com.example.backend.reservation;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // ✅ contentid 타입을 String으로
    List<Reservation> findByContentidOrderByReservationDateDesc(String contentid);
}
