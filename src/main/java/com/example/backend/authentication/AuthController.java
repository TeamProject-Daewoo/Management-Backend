package com.example.backend.authentication;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserDto.SignUp signUpDto) {
        userService.signUp(signUpDto);
        return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto.Login loginDto) {
    	try {
        // 1. UserService에서 토큰 정보 받아오기
        TokenInfo tokenInfo = userService.login(loginDto);

        // 2. Refresh Token을 HttpOnly 쿠키로 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokenInfo.getRefreshToken())
                .httpOnly(true)
                //.secure(true) // HTTPS에서만 전송 (배포시에는 주석 해제)
                .path("/")
                .sameSite("Lax")
                .maxAge(60 * 60 * 24 * 30 * 6)
                .build();

        // 3. Access Token만 DTO에 담아 Body로 반환
        UserDto.AccessTokenResponse accessTokenResponse = new UserDto.AccessTokenResponse(tokenInfo.getAccessToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(accessTokenResponse);
    	} catch (AuthenticationException e) {
            // 👇 모든 인증 관련 예외(비밀번호 오류, 계정 잠금 등)를 여기서 한 번에 처리
            // e.getMessage()를 통해 서비스에서 던진 메시지를 그대로 사용
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // 쿠키의 만료 시간을 0으로 설정하여 삭제
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", null)
                .maxAge(0)
                .path("/")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body("로그아웃되었습니다.");
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<UserDto.AccessTokenResponse> refresh(@CookieValue("refreshToken") String refreshToken) {
        // 쿠키에서 Refresh Token을 가져와 새로운 Access Token 발급
        String newAccessToken = userService.reissueAccessToken(refreshToken);
        UserDto.AccessTokenResponse accessTokenResponse = new UserDto.AccessTokenResponse(newAccessToken);
        return ResponseEntity.ok(accessTokenResponse);
    }
    
    @PostMapping("/check-username")
    public ResponseEntity<String> checkUsername(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        if (userService.isUsernameExists(username)) {
            // 이미 존재하면 409 Conflict 에러 반환
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 아이디입니다.");
        } else {
            // 존재하지 않으면 200 OK 반환
            return ResponseEntity.ok("사용 가능한 아이디입니다.");
        }
    }
}