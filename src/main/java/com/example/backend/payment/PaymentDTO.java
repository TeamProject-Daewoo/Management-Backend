package com.example.backend.payment;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class PaymentDTO {
  private Long paymentId;
  private Long reservationId;
  private String userName;
  private Integer amount;
  private String method;
  private String status;
  private LocalDateTime paidAt;
}
