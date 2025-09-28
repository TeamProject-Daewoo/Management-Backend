package com.example.backend.reservation;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.backend.admindashboard.AdminDashboardReservationDto;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  List<Reservation> findByHotel_ContentidOrderByReservationDateDesc(String contentid);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update Reservation r set r.status = :status where r.reservationId in :ids")
  int updateStatus(@Param("ids") Collection<Long> ids, @Param("status") String status);

  @Query("SELECT new com.example.backend.admindashboard.AdminDashboardReservationDto(" +
        "res.reservationId, res.checkInDate, res.checkOutDate, res.roomcode, "+
        "res.hotel.contentid, res.status, res.totalPrice, res.reservName, res.user.username) " +
        "FROM Reservation res WHERE res.hotel.contentid IN :hotelIds")
  List<AdminDashboardReservationDto> getReservationsByHotelIds(@Param("hotelIds") List<String> hotelIds);
}
