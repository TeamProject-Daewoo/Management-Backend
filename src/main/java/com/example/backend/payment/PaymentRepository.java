package com.example.backend.payment;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

  List<Payment> findByReservationReservationId(Long reservationId);

  List<Payment> findByReservationReservationIdIn(Collection<Long> reservationIds);

  // 최신 결제 1건
  Optional<Payment> findTopByReservationReservationIdOrderByPaymentDateDesc(Long reservationId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update Payment p set p.paymentStatus = :status where p.reservation.reservationId in :ids")
  int updatePaymentStatus(@Param("ids") Collection<Long> reservationIds, @Param("status") String status);
}
