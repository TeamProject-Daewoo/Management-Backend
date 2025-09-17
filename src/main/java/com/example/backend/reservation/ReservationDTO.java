// com.example.backend.reservation.ReservationDTO
package com.example.backend.reservation;
import lombok.*; import java.time.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class ReservationDTO {
  private Long reservationId;
  private String userName;      // 로그인 아이디
  private String bookerName;    // reserv_name
  private String phone;         // reserv_phone
  private LocalDate checkInDate;
  private LocalDate checkOutDate;
  private String roomcode;
  private String status;
  private Integer totalPrice;
  private LocalDateTime reservationDate;
}
