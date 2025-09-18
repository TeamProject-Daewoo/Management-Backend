package com.example.backend.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.backend.authentication.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reservations")
@Getter
@Setter
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_id")
  private Long reservationId;

  // FK → hotels.id (bigint)
  @Column(name = "contentid")
  private Long contentid;

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
  @JoinColumn(name = "user_name", referencedColumnName = "user_name")
  private User user;
}
