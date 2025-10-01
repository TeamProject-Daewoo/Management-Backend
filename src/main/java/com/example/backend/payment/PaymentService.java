package com.example.backend.payment;


import com.example.backend.authentication.User;
import com.example.backend.authentication.UserRepository;
import com.example.backend.mail.EmailService;
import com.example.backend.reservation.Reservation;
import com.example.backend.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Value("${toss.widget-secret-key}")
    private String tossWidgetSecretKey;

    @Transactional
    public String cancelPayment(Long reservationId, String cancelReason) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약을 찾을 수 없습니다."));

        if (!"PAID".equals(reservation.getStatus())) {
            throw new IllegalStateException("결제가 완료된 예약만 취소할 수 있습니다.");
        }
        System.out.println("1-----------------------");

        LocalDate today = LocalDate.now();
        LocalDate checkInDate = reservation.getCheckInDate();

        if (today.isAfter(checkInDate) || today.isEqual(checkInDate)) {
            throw new IllegalStateException("체크인 날짜가 지났거나 당일인 예약은 취소할 수 없습니다.");
        }

        System.out.println("2-----------------------");
        Payment payment = paymentRepository.findByReservation(reservation)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보를 찾을 수 없습니다."));

                System.out.println("3-----------------------");
        long daysBeforeCheckIn = ChronoUnit.DAYS.between(today, checkInDate);
        int originalAmount = payment.getPaymentAmount();
        int cancelFee = calculateCancelFee(originalAmount, daysBeforeCheckIn);
        int refundAmount = originalAmount - cancelFee;

        String url = "https://api.tosspayments.com/v1/payments/" + payment.getPaymentKey() + "/cancel";

        HttpHeaders headers = new HttpHeaders();
        String encodedAuth = new String(Base64.getEncoder().encode((tossWidgetSecretKey + ":").getBytes(StandardCharsets.UTF_8)));
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("cancelReason", cancelReason);
        if (cancelFee > 0) {
            body.put("cancelAmount", refundAmount);
        }

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        
        try {
            
            System.out.println("4-----------------------");
            restTemplate.postForObject(url, request, Map.class);
            
            System.out.println("5-----------------------");

            if (reservation.getUsedPoints() != null && reservation.getUsedPoints() > 0) {
                User user = reservation.getUser();
                if (user != null) {
                    // 포인트 환불
                    int currentPoints = user.getPoint() != null ? user.getPoint() : 0;
                    user.addPoints(currentPoints + reservation.getUsedPoints());
                    userRepository.save(user);
                }
            }

            reservation.setStatus("CANCELLED");
            payment.setPaymentStatus("CANCELED");

            reservationRepository.save(reservation);
            paymentRepository.save(payment);

            emailService.sendCancellationConfirmationEmail(reservation, refundAmount, cancelFee);


            return String.format("취소가 완료되었습니다. 환불 금액: %,d원 (수수료: %,d원)", refundAmount, cancelFee);

        }  catch (HttpClientErrorException e) {
            // 4xx 에러가 발생했을 때 상세 정보 출력
            System.err.println("HTTP 상태 코드: " + e.getStatusCode());
            System.err.println("응답 본문 (Response Body): " + e.getResponseBodyAsString());
            
            throw e;
        } catch (Exception e) {
            System.err.println(e);
            throw e;
        }
        
    }

    public int calculateCancelFee(int totalAmount, long daysBeforeCheckIn) {
        if (daysBeforeCheckIn >= 3) {
            return 0;
        } else if (daysBeforeCheckIn == 2) {
            return (int) (totalAmount * 0.2);
        } else if (daysBeforeCheckIn == 1) {
            return (int) (totalAmount * 0.5);
        }
        return totalAmount;
    }
}
