package com.example.backend.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ReservationDTO {
  private Long reservationId;
  private String userName;       // 로그인 아이디
  private String bookerName;     // 표시 이름 (users.name)
  private String email;
  private String phone;
  private LocalDate checkInDate;
  private LocalDate checkOutDate;
  private String roomcode;
  private String status;
  private Integer totalPrice;
  private LocalDateTime reservationDate;
}
