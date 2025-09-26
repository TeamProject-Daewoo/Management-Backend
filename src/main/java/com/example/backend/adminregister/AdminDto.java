package com.example.backend.adminregister;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

public class AdminDto {
	
    @Data
    public static class CreateRequest {
        private String name;
        private String adminId;
        private String password;
        private AdminRole role;
    }
    
    @Data
    @Builder
    public static class Info {
        private Long id;
        private String adminId;
        private String name;
        private AdminRole role;
        private LocalDateTime createdAt;

        public static Info fromEntity(Admin admin) {
            return Info.builder()
                    .id(admin.getId())
                    .adminId(admin.getAdminId())
                    .name(admin.getName())
                    .role(admin.getRole())
                    .createdAt(admin.getCreatedAt())
                    .build();
        }
    }

}