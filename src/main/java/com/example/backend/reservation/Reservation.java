package com.example.backend.reservation;

import com.example.backend.user.User;
import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
import java.time.LocalDate; import java.time.LocalDateTime;

@Entity @Table(name="reservations")
@Getter @Setter
public class Reservation {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="reservation_id") private Long reservationId;

  // FK to hotels.id (BIGINT)
  @Column(name="contentid") private Long contentid;

  @Column(name="roomcode") private String roomcode;

  @Column(name="check_in_date") private LocalDate checkInDate;
  @Column(name="check_out_date") private LocalDate checkOutDate;

  @Column(name="num_adults") private Integer numAdults;
  @Column(name="num_children") private Integer numChildren;

  @Column(name="status") private String status;
  @Column(name="total_price") private Integer totalPrice;

  @Column(name="reservation_date") private LocalDateTime reservationDate;

  @Column(name="reserv_name") private String reservName;
  @Column(name="reserv_phone") private String reservPhone;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="user_name", referencedColumnName = "user_name")
  private User user;
}
