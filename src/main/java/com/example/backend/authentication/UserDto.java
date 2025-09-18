package com.example.backend.authentication; // 본인의 DTO 패키지 경로로 수정

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDto {

    /**
     * 회원가입 요청을 위한 DTO
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SignUp {
        private String username;
        private String password;
        private String name;
        private String phoneNumber;
        private String business_registration_number;
        private String loginType;
        private String uuid;
        private Role role;

        // DTO를 User 엔티티로 변환하는 메소드
        public User toEntity(String encodedPassword) {
            return User.builder()
                    .username(this.username)
                    .password(encodedPassword) // 암호화된 비밀번호 사용
                    .name(this.name)
                    .phoneNumber(this.phoneNumber)
                    .business_registration_number(this.business_registration_number)
                    .loginType(this.loginType)
                    .uuid(this.uuid)
                    .role(this.role)
                    .build();
        }
    }

    /**
     * 로그인 요청을 위한 DTO
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Login {
        private String username;
        private String password;
    }

    /**
     * 사용자 정보 응답을 위한 DTO
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Info {
        private String username;
        private String name;
        private String phoneNumber;
        private Role role;
        private ApprovalStatus approvalStatus;

        // User 엔티티를 Info DTO로 변환하는 정적 메소드
        public static Info from(User user) {
            return Info.builder()
                    .username(user.getUsername())
                    .name(user.getName())
                    .phoneNumber(user.getPhoneNumber())
                    .role(user.getRole())
                    .approvalStatus(user.getApprovalStatus())
                    .build();
        }
    }

    /**
     * Access Token 응답을 위한 DTO
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccessTokenResponse {
        private String accessToken;
    }
}