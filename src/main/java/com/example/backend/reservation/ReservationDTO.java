package com.example.backend.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

  private Long reservationId;

  private String userName;

  private String userDisplayName;

  private String userEmail;

  private String userPhone;

  private String reservName;

  private String reservPhone;

  private LocalDate checkInDate;

  private LocalDate checkOutDate;

  private String roomcode;

  private String roomtitle;
  
  private String status;

  private Integer totalPrice;

  private LocalDateTime reservationDate;
}
