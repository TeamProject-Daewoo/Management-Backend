package com.example.backend.CustomerService;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InquiryAnswerRepository extends JpaRepository<InquiryAnswer, Long> {
    Optional<InquiryAnswer> findByInquiryId(Long inquiryId);
}
