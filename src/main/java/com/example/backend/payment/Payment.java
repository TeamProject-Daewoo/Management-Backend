package com.example.backend.payment;

import java.time.LocalDateTime;
import com.example.backend.reservation.Reservation;
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
@Table(name = "payments")
@Getter
@Setter
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_id")
  private Long paymentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reservation_id")
  private Reservation reservation;

  @Column(name = "user_name")
  private String userName;

  @Column(name = "payment_key")
  private String paymentKey;

  @Column(name = "payment_amount")
  private Integer paymentAmount;

  @Column(name = "payment_method")
  private String paymentMethod;

  @Column(name = "payment_status")
  private String paymentStatus;

  @Column(name = "payment_date")
  private LocalDateTime paymentDate;
}
