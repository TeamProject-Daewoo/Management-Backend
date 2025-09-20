package com.example.backend.CustomerService;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserInquiryRepository extends JpaRepository<UserInquiryDto, String> {
    // 관리자 username으로 찾기
     Optional<UserInquiryDto> findByUsername(String username);
}
