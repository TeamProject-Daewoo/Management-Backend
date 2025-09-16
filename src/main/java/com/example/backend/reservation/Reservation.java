package com.example.backend.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.example.backend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name="reservations")
@Getter @Setter
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="reservation_id")
  private Long reservationId;

  @Column(name="user_name", nullable=false)
  private String userName;      // FK -> users.user_name (연관 매핑 대신 단순 문자열 보관)

  @Column(name="contentid", nullable=false)
  private String contentid;     // FK -> hotels.contentid (varchar)

  private String roomcode;

  @Column(name="check_in_date")
  private LocalDate checkInDate;

  @Column(name="check_out_date")
  private LocalDate checkOutDate;

  @Column(name="num_adults")
  private Integer numAdults;

  @Column(name="num_children")
  private Integer numChildren;

  private String status;

  @Column(name="total_price")
  private Integer totalPrice;

  @Column(name="reservation_date")
  private LocalDateTime reservationDate;

  // 필요하면 연관관계도 추가 가능 (지금은 조회용으로 User를 fetch하지 않아도 됨)
  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name="user_name", referencedColumnName="user_name", insertable=false, updatable=false)
  private User user;
}
