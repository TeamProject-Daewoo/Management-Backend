package com.example.backend.CustomerService;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InquiryAnswerService {

    private final InquiryRepository inquiryRepository;
    private final InquiryAnswerRepository inquiryAnswerRepository;
    private final UserInquiryRepository userRepository;

    @Transactional
    public void saveAnswer(Long inquiryId, String answerContent, String adminUsername) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("문의가 존재하지 않습니다."));

        UserInquiryDto adminUser = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new IllegalArgumentException("관리자 사용자를 찾을 수 없습니다."));

        InquiryAnswer answer = inquiryAnswerRepository.findByInquiryId(inquiryId)
                .orElseGet(() -> InquiryAnswer.create(inquiry, answerContent, adminUser));

        answer.setAnswerContent(answerContent);
        answer.setAnsweredAt(LocalDateTime.now());
        answer.setAdminUser(adminUser);

        inquiryAnswerRepository.save(answer);

        inquiry.markAsAnswered();
        inquiryRepository.save(inquiry);
    }
}
