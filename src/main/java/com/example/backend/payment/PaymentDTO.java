// com.example.backend.payment.PaymentDTO
package com.example.backend.payment;
import lombok.*; import java.time.*;
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
