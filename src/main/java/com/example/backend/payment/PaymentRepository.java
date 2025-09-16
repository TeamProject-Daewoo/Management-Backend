package com.example.backend.payment;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
  List<Payment> findByReservationReservationIdIn(List<Long> reservationIds);
}
