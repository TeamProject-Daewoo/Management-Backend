package com.example.backend.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

  // ✅ 프론트 매칭/디버그용 roomId 추가
  private Long roomId;

  private String status;
  private Integer totalPrice;
  private LocalDateTime reservationDate;

  private Integer numAdults;
  private Integer numChildren;

  // ✅ 결제
  private String paymentStatus;
  private Long paymentId;
  private LocalDateTime paymentDate;
  private Integer paymentAmount;

  // ✅ 호텔 식별/표시
  private String contentId;   // 호텔 contentid
  private String hotelTitle;  // 호텔명

  /** 엔티티 → DTO (필드 순서 안전: Builder 사용) */
  public static ReservationDTO from(Reservation r) {
    if (r == null) return null;

    ReservationDTO.ReservationDTOBuilder b = ReservationDTO.builder()
        .reservationId(r.getReservationId())
        .reservName(r.getReservName())
        .reservPhone(r.getReservPhone())
        .checkInDate(r.getCheckInDate())
        .checkOutDate(r.getCheckOutDate())
        .roomcode(r.getRoomcode())
        .status(r.getStatus())
        .totalPrice(r.getTotalPrice())
        .reservationDate(r.getReservationDate())
        .numAdults(r.getNumAdults())
        .numChildren(r.getNumChildren());

    // 사용자
    if (r.getUser() != null) {
      b.userName(r.getUser().getUsername())
       .userDisplayName(r.getUser().getName())
       .userPhone(r.getUser().getPhoneNumber());
      // userEmail은 테이블 없으면 null 유지
    }

    // 호텔
    if (r.getHotel() != null) {
      b.contentId(r.getHotel().getContentid())
       .hotelTitle(r.getHotel().getTitle());
    }

    // 결제/roomtitle/roomId는 Service에서 보강 세팅 (기본 null)
    return b.build();
  }
}
