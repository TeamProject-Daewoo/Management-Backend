package com.example.backend.authentication;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     */
    @Transactional
    public void signUp(UserDto.SignUp signUpDto) {
        if (userRepository.findByUsername(signUpDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        User user = signUpDto.toEntity(encodedPassword);
        userRepository.save(user);
    }

    /**
     * 로그인
     */
    @Transactional
    public TokenInfo login(UserDto.Login loginDto) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
    }

    @Transactional
    public String reissueAccessToken(String refreshToken) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        // 2. Refresh Token에서 username 가져오기
        String username = jwtTokenProvider.getUsername(refreshToken);

        // 3. DB에서 사용자 정보 조회
        UserDetails userDetails = loadUserByUsername(username);

        // 4. 새로운 Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

        // 5. 새로운 Access Token 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // (선택) DB/Redis에 Refresh Token 업데이트 로직 추가
        
        return tokenInfo.getAccessToken();
    }

    /**
     * Spring Security가 사용자를 인증할 때 사용하는 메소드
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	System.out.println("사용자쪽 UserDetails 가 실행되었습니다");
        User user = userRepository.findByUsername(username)
                // 👇 1. 사용자를 찾지 못하면 BadCredentialsException 발생
                .orElseThrow(() -> new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다."));
        
        // 👇 2. 사용자는 찾았지만, 계정이 잠겨있으면(승인 대기 중이면) LockedException 발생
        if (!user.isAccountNonLocked()) {
            throw new LockedException("아직 승인되지 않은 계정입니다.");
        }

        return user;
    }

    public UserDto.Info getUserInfo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 아이디를 찾을 수 없습니다."));
        return UserDto.Info.from(user);
    }

    @Transactional(readOnly = true)
    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public List<UserDto.AdminList> findAllAdmins() { 
        // 👇 검색할 모든 관리자 Role을 리스트로 정의합니다.
        List<Role> adminRoles = List.of(
            Role.ADMIN_SUPER, 
            Role.ADMIN_CS, 
            Role.ADMIN_BIZ
        );
        
        // 👇 정의한 Role 리스트를 Repository 메서드로 전달합니다.
        List<User> admins = userRepository.findByRoleInOrderByJoinDateDesc(adminRoles);
        
        return admins.stream()
                .map(UserDto.AdminList::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional // 데이터베이스 변경이 있으므로 트랜잭션 처리
    public void deleteUser(String username) {
        // 1. 현재 로그인된 관리자의 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // 2. 자기 자신을 삭제하려는 시도인지 확인합니다. (매우 중요)
        if (currentUsername.equals(username)) {
            throw new IllegalArgumentException("자기 자신의 계정은 삭제할 수 없습니다.");
        }
        
        // 3. 데이터베이스에서 삭제할 사용자를 찾습니다.
        User userToDelete = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 가진 사용자를 찾을 수 없습니다."));

        // 4. 사용자를 삭제합니다.
        userRepository.delete(userToDelete);
    }
}