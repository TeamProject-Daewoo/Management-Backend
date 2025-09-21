package com.example.backend.reservation;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  List<Reservation> findByContentidOrderByReservationDateDesc(String contentid);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update Reservation r set r.status = :status where r.reservationId in :ids")
  int updateStatus(@Param("ids") Collection<Long> ids, @Param("status") String status);
}
