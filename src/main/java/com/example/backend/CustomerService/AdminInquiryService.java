package com.example.backend.CustomerService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminInquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryAnswerRepository inquiryAnswerRepository;

    public Page<Inquiry> getInquiries(Pageable pageable, InquiryStatus status) {
        if (status != null) {
            return inquiryRepository.findAllByStatus(status, pageable);
        }
        return inquiryRepository.findAll(pageable);
    }

    public InquiryDetailDto getInquiryDetail(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findByIdWithFiles(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("문의가 존재하지 않습니다."));

        InquiryAnswer answer = inquiryAnswerRepository.findByInquiryId(inquiryId).orElse(null);

        return InquiryDetailDto.from(inquiry, answer);
    }

    public Inquiry getInquiryById(Long id) {
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
    }
}
