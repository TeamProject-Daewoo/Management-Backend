package com.example.backend.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.backend.authentication.User;
import com.example.backend.coupon.entity.Coupon;
import com.example.backend.hotel.Hotel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reservations", indexes = {
    @Index(name = "idx_fk_contentid", columnList = "contentid"),
    @Index(name = "idx_fk_user_name", columnList = "user_name"),
    @Index(name = "idx_check_in_date", columnList = "check_in_date"),
    @Index(name = "idx_check_out_date", columnList = "check_out_date")
})
@Getter
@Setter
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_id")
  private Long reservationId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "contentid")
  private Hotel hotel;

  @Column(name = "roomcode")
  private String roomcode;

  @Column(name = "check_in_date")
  private LocalDate checkInDate;

  @Column(name = "check_out_date")
  private LocalDate checkOutDate;

  @Column(name = "num_adults")
  private Integer numAdults;

  @Column(name = "num_children")
  private Integer numChildren;

  @Column(name = "status")
  private String status;

  @Column(name = "total_price")
  private Integer totalPrice;

  @Column(name = "reservation_date")
  private LocalDateTime reservationDate;

  @Column(name = "reserv_name")
  private String reservName;

  @Column(name = "reserv_phone")
  private String reservPhone;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "used_coupon_id")
  private Coupon usedCoupon;

  @Column(name = "used_points")
  private Integer usedPoints;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_name", referencedColumnName = "user_name")
  private User user;
}
