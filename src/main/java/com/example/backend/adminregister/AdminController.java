package com.example.backend.adminregister;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN_SUPER')") // 최고 관리자만 접근 가능
    public ResponseEntity<String> createAdmin(@RequestBody AdminDto.CreateRequest request) {
        adminService.createAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("관리자 계정이 성공적으로 생성되었습니다.");
    }

    @PostMapping("/check-id")
    @PreAuthorize("hasRole('ROLE_ADMIN_SUPER')")
    public ResponseEntity<String> checkAdminId(@RequestBody Map<String, String> payload) {
        if (adminService.checkAdminIdExists(payload.get("adminId"))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 아이디입니다.");
        }
        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN_SUPER')") // 최고 관리자만 접근 가능
    public ResponseEntity<List<AdminDto.Info>> getAdminList() {
        List<AdminDto.Info> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }
}