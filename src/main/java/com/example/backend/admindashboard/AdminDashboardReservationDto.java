package com.example.backend.admindashboard;

import java.time.LocalDate;

import com.example.backend.reservation.Reservation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminDashboardReservationDto {
    private Long reservationId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String roomcode;
    private String contentId;
    private String status;
    private Integer totalPrice;
    private String reservName;
    private String email;

    public AdminDashboardReservationDto(Reservation reservation) {
        this.reservationId = reservation.getReservationId();
        this.checkInDate = reservation.getCheckInDate();
        this.checkOutDate = reservation.getCheckOutDate();
        this.roomcode = reservation.getRoomcode();
        this.contentId = reservation.getHotel().getContentid();
        this.status = reservation.getStatus();
        this.totalPrice = reservation.getTotalPrice();
        this.reservName = reservation.getReservName();
        this.email = reservation.getUser().getUsername();
    }

}
