package com.example.backend.approveuser;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@PreAuthorize("hasRole('ADMIN_MASTER')")
@RequestMapping("/api/admin/business-users")
@RequiredArgsConstructor
public class ApproveUserController {

    private final ApproveUserService approveUserService;

    // GET /api/admin/business-users : 사업자 목록 조회
    @GetMapping
    public ResponseEntity<List<ApproveUserListDto>> getBusinessUserList() {
        return ResponseEntity.ok(approveUserService.getBusinessUsersForList());
    }

    // GET /api/admin/business-users/{username} : 사업자 상세 정보 조회
    @GetMapping("/{username}")
    public ResponseEntity<ApproveUserDetailDto> getBusinessUserDetails(@PathVariable String username) {
        return ResponseEntity.ok(approveUserService.getBusinessUserDetails(username));
    }

    // PATCH /api/admin/business-users/{username}/approve : 사용자 승인
    @PatchMapping("/{username}/approve")
    public ResponseEntity<Void> approveUser(@PathVariable String username) {
    	approveUserService.approveUser(username);
        return ResponseEntity.ok().build();
    }
    
    // PATCH /{username}/to-pending : 대기 상태로 변경
    @PatchMapping("/{username}/to-pending")
    public ResponseEntity<Void> changeToPending(@PathVariable String username) {
    	approveUserService.changeToPending(username);
        return ResponseEntity.ok().build();
    }
    
 // PATCH /{username}/to-rejected : 차단(거절) 상태로 변경
    @PatchMapping("/{username}/to-rejected")
    public ResponseEntity<Void> changeToRejected(@PathVariable String username) {
    	approveUserService.changeToRejected(username);
        return ResponseEntity.ok().build();
    }

    // DELETE /api/admin/business-users/{username} : 사용자 거절 (삭제)
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
    	approveUserService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}