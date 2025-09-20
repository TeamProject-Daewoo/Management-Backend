package com.example.backend.approveuser;

import java.time.LocalDateTime;

import com.example.backend.authentication.ApprovalStatus;
import com.example.backend.authentication.User;

import lombok.Getter;

@Getter
public class ApproveUserDetailDto {
	private String username;
    private String name; // 상세 보기에도 이름(회사명)이 있으면 좋음
    private String phoneNumber;
    private LocalDateTime joinDate;
    private String businessRegistrationNumber;
    private ApprovalStatus approvalStatus;

    public ApproveUserDetailDto(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.phoneNumber = user.getPhoneNumber();
        this.joinDate = user.getJoinDate();
        this.businessRegistrationNumber = user.getBusiness_registration_number();
        this.approvalStatus = user.getApprovalStatus();
    }
}
