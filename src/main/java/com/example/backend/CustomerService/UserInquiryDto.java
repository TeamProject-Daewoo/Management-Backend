package com.example.backend.CustomerService;

import com.example.backend.authentication.Role;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInquiryDto {
    private String username;
    private String name;
    private Role role;

    public boolean isAdmin() {
        return this.role != null && this.role.name().startsWith("ADMIN_");
    }

    // 정적 팩토리 메서드로 Entity → DTO 변환
    public static UserInquiryDto from(com.example.backend.authentication.User user) {
        return UserInquiryDto.builder()
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}
