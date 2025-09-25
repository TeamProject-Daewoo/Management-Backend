package com.example.backend.approveuser;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.authentication.ApprovalStatus;
import com.example.backend.authentication.Role;
import com.example.backend.authentication.User;
import com.example.backend.authentication.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApproveUserService {

	 private final UserRepository userRepository;

	    /**
	     * 사업자 사용자 '목록' 조회 (목록용 DTO 사용)
	     */
	    public List<ApproveUserListDto> getBusinessUsersForList() {
	        List<ApprovalStatus> statuses = List.of(ApprovalStatus.PENDING, ApprovalStatus.APPROVED, ApprovalStatus.REJECTED);
	        List<User> users = userRepository.findByRoleAndApprovalStatusIn(Role.BUSINESS, statuses);
	        return users.stream()
	                .map(ApproveUserListDto::new)
	                .collect(Collectors.toList());
	    }
	    
	    /**
	     * 사업자 사용자 '상세 정보' 조회 (상세보기용 DTO 사용)
	     */
	    public ApproveUserDetailDto getBusinessUserDetails(String username) {
	        User user = userRepository.findById(username)
	                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. Username: " + username));
	        
	        return new ApproveUserDetailDto(user);
	    }

	    /**
	     * 사용자 승인 (username 기반)
	     */
	    @Transactional
	    public void approveUser(String username) {
	        User user = findBusinessUser(username);
	        user.setApprovalStatus(ApprovalStatus.APPROVED);
	    }

	    /**
	     * 사용자 거절 (PENDING -> REJECTED)
	     */
	    @Transactional
	    public void rejectUser(String username) {
	        User user = findBusinessUser(username);
	        user.setApprovalStatus(ApprovalStatus.REJECTED);
	    }
	    
	    /**
	     * 사용자 최종 삭제 (DB에서 제거)
	     */
	    @Transactional
	    public void deleteUser(String username) {
	        if (!userRepository.existsById(username)) {
	            throw new EntityNotFoundException("삭제할 사용자를 찾을 수 없습니다. Username: " + username);
	        }
	        userRepository.deleteById(username);
	    }

	    /**
	     * 사용자를 '대기' 상태로 변경 (APPROVED -> PENDING)
	     */
	    @Transactional
	    public void changeToPending(String username) {
	        User user = findBusinessUser(username);
	        user.setApprovalStatus(ApprovalStatus.PENDING);
	    }

	    /**
	     * 사용자를 '차단(거절)' 상태로 변경 (APPROVED -> REJECTED)
	     */
	    @Transactional
	    public void changeToRejected(String username) {
	        User user = findBusinessUser(username);
	        user.setApprovalStatus(ApprovalStatus.REJECTED);
	    }

	    // --- Private Helper Method ---
	    private User findBusinessUser(String username) {
	        User user = userRepository.findById(username)
	            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. Username: " + username));
	        if (user.getRole() != Role.BUSINESS) {
	            throw new IllegalStateException("사업자 회원이 아닙니다.");
	        }
	        return user;
	    }
}
