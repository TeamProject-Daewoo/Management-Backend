package com.example.backend.coupon.controller;

import com.example.backend.authentication.User;
import com.example.backend.coupon.dto.CouponDto;
import com.example.backend.coupon.dto.UserCouponDto;
import com.example.backend.coupon.entity.Coupon;
import com.example.backend.coupon.service.CouponService;
import com.example.backend.authentication.UserRepository;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final UserRepository userRepository;

    // 관리자용 전체 쿠폰 조회
    @GetMapping("/list")
    public ResponseEntity<Page<CouponDto>> getAllCouponsForAdmin(Pageable pageable) {
        return ResponseEntity.ok(couponService.getAllCouponsForAdmin(pageable));
    }

    // [운영자] 쿠폰 등록
    @PostMapping
public ResponseEntity<CouponDto> createCoupon(
        @AuthenticationPrincipal org.springframework.security.core.userdetails.User loginUser,
        @RequestBody CouponDto dto
) {
    return ResponseEntity.ok(couponService.createCoupon(dto, loginUser));
}

    // [운영자/이벤트 페이지] 현재 유효한 쿠폰 목록 조회 (페이징)
    @GetMapping("/valid")
    public ResponseEntity<Page<CouponDto>> getValidCoupons(Pageable pageable) {
        return ResponseEntity.ok(couponService.getValidCoupons(pageable));
    }

    // [유저] 내 쿠폰함 조회
    @GetMapping("/my")
    public ResponseEntity<Page<UserCouponDto>> getMyCoupons(
            @AuthenticationPrincipal User user,
            Pageable pageable
    ) {
        return ResponseEntity.ok(couponService.getUserCoupons(user.getUsername(), pageable));
    }

    // [유저] 사용 가능한 쿠폰 조회
    @GetMapping("/my/available")
    public ResponseEntity<Page<UserCouponDto>> getMyAvailableCoupons(
            @AuthenticationPrincipal User user,
            Pageable pageable
    ) {
        return ResponseEntity.ok(couponService.getAvailableUserCoupons(user.getUsername(), pageable));
    }

    // [이벤트/코드 입력] 쿠폰 코드로 발급
    @PostMapping("/issue/code")
    public ResponseEntity<UserCouponDto> issueCouponByCode(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
            @RequestParam String couponCode,
            @RequestParam(required = false, defaultValue = "CODE_INPUT") String source
    ) {
        CouponDto couponDto = couponService.getCouponByCode(couponCode);
        Coupon coupon = new Coupon();
        coupon.setId(couponDto.getId());
        return ResponseEntity.ok(
                couponService.issueCouponToUser(user, coupon, source)
        );
    }

    @PostMapping("/issue/manual")
    public ResponseEntity<UserCouponDto> issueCouponManual(
            @RequestParam String username,
            @RequestParam Long couponId,
            @RequestParam(required = false, defaultValue = "ADMIN") String source
    ) {
        // 임시 Security User 객체 생성 (username만 필요)
        org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(username, "", new ArrayList<>());
        Coupon coupon = new Coupon();
        coupon.setId(couponId);
        return ResponseEntity.ok(
                couponService.issueCouponToUser(securityUser, coupon, source)
        );
    }


}
