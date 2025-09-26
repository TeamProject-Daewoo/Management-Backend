package com.example.backend.adminregister;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.authentication.UserDto;
import com.example.backend.authentication.UserDto.AdminList;
import com.example.backend.authentication.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminCreateController {

    private final UserService userService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // 최고 관리자만 접근 가능
    public ResponseEntity<String> signUp(@RequestBody UserDto.SignUp signUpDto) {
        userService.signUp(signUpDto);
        return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
    }

 // 아이디 중복 확인 API
    @PostMapping("/check-id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> checkAdminId(@RequestBody Map<String, String> payload) {
        if (userService.isUsernameExists(payload.get("username"))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 아이디입니다.");
        }
        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }
    
 // 관리자 목록 조회 API
    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AdminList>> getAdminList() {
        // 👇 'ADMIN' 역할을 가진 모든 사용자를 찾는 로직으로 변경
        List<AdminList> admins = userService.findAllAdmins();
        return ResponseEntity.ok(admins);
    }
    
    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteAdmin(@PathVariable String username) {
        try {
            userService.deleteUser(username);
            return ResponseEntity.ok("관리자 계정이 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            // 존재하지 않는 사용자를 삭제하려 하거나, 자신을 삭제하려 할 때
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // 그 외 서버 에러
            return ResponseEntity.internalServerError().body("계정 삭제 중 오류가 발생했습니다.");
        }
    }
}