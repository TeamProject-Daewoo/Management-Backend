package com.example.backend.mail;

import com.example.backend.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender mailSender;

    @Async
    public void sendCancellationConfirmationEmail(Reservation reservation, int refundAmount, int cancelFee) {
        if (reservation.getUser() == null || reservation.getUser().getUsername() == null) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(reservation.getUser().getUsername());
        message.setSubject("[HotelHub] 예약이 취소되었습니다.");

        String emailText = String.format(
                "안녕하세요, %s님.\n\n" +
                        "요청하신 예약이 정상적으로 취소되었음을 알려드립니다.\n\n" +
                        "■ 예약 번호: %d\n" +
                        "■ 숙소명: %s\n" +
                        "■ 취소 수수료: %,d원\n" +
                        "■ 최종 환불 금액: %,d원\n\n" +
                        "환불은 영업일 기준 3~5일 이내에 처리됩니다.\n" +
                        "이용해주셔서 감사합니다.",
                reservation.getUser().getName(),
                reservation.getReservationId(),
                reservation.getHotel().getTitle(),
                cancelFee,
                refundAmount
        );

        message.setText(emailText);

        mailSender.send(message);
    }
}