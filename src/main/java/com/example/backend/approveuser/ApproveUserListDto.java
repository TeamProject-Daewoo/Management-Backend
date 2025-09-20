package com.example.backend.approveuser;

import com.example.backend.authentication.ApprovalStatus;
import com.example.backend.authentication.User;

import lombok.Getter;

@Getter
public class ApproveUserListDto {
	private String username;
    private String name; // Entity의 name 필드 (회사명으로 사용)
    private ApprovalStatus approvalStatus;

    public ApproveUserListDto(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.approvalStatus = user.getApprovalStatus();
    }
}
