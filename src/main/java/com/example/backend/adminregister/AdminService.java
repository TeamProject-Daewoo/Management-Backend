package com.example.backend.adminregister;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createAdmin(AdminDto.CreateRequest request) {
        if (adminRepository.findByAdminId(request.getAdminId()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        
        Admin admin = Admin.builder()
                .name(request.getName())
                .adminId(request.getAdminId())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        
        adminRepository.save(admin);
    }
    
    @Transactional(readOnly = true)
    public boolean checkAdminIdExists(String adminId) {
        return adminRepository.findByAdminId(adminId).isPresent();
    }
    
    @Transactional(readOnly = true)
    public List<AdminDto.Info> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(AdminDto.Info::fromEntity)
                .collect(Collectors.toList());
    }
}