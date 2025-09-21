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

  private Integer numAdults;
  private Integer numChildren;

  // ✅ 결제 관련 필드 추가
  private String paymentStatus;
  private Long paymentId;
  private LocalDateTime paymentDate;
  private Integer paymentAmount;
  public static ReservationDTO from(Reservation r) {
    if (r == null) return null;

    return new ReservationDTO(
        r.getReservationId(),
        r.getUser() != null ? r.getUser().getUsername() : null,   // userName
        r.getUser() != null ? r.getUser().getName() : null,       // userDisplayName
        null,                                                     // userEmail (테이블에 없으면 null 유지)
        r.getUser() != null ? r.getUser().getPhoneNumber() : null,// userPhone
        r.getReservName(),
        r.getReservPhone(),
        r.getCheckInDate(),
        r.getCheckOutDate(),
        r.getRoomcode(),
        null,                                                     // roomtitle은 HotelAdminService.toDtoWithPayment에서 보강됨
        r.getStatus(),
        r.getTotalPrice(),
        r.getReservationDate(),
        r.getNumAdults(),
        r.getNumChildren(),
        null,                                                     // paymentStatus (service에서 세팅)
        null,                                                     // paymentId
        null,                                                     // paymentDate
        null                                                      // paymentAmount
    );
}
}
