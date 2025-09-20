package com.example.backend.CustomerService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryFileService {

    private final InquiryFileRepository inquiryFileRepository;

    public InquiryFile getById(Long id) {
        return inquiryFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다. id=" + id));
    }

    // 필요하다면 다른 파일 관련 비즈니스 로직 추가 가능
}
