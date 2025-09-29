package com.example.backend.coupon.service;

import com.example.backend.authentication.*;
import com.example.backend.authentication.UserRepository;
import com.example.backend.coupon.dto.CouponDto;
import com.example.backend.coupon.dto.UserCouponDto;
import com.example.backend.coupon.entity.Coupon;
import com.example.backend.coupon.entity.UserCoupon;
import com.example.backend.coupon.repository.CouponRepository;
import com.example.backend.coupon.repository.UserCouponRepository;
import com.example.backend.hotel.Hotel;
import com.example.backend.hotel.HotelRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final HotelRepository hotelRepository;

    public CouponDto getCouponByCode(String couponCode) {
        Coupon coupon = couponRepository.findByCouponCode(couponCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰 코드입니다."));

        return CouponDto.fromEntity(coupon);
    }

    public Page<CouponDto> getValidCoupons(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        return couponRepository
                .findByIsActiveTrueAndValidFromBeforeAndValidToAfter(now, now, pageable)
                .map(CouponDto::fromEntity);
    }

    public Page<CouponDto> getAllCouponsForAdmin(Pageable pageable) {
        return couponRepository.findAllCoupons(pageable)
                .map(CouponDto::fromEntity);
    }

    public Page<UserCouponDto> getUserCoupons(String username, Pageable pageable) {
        return userCouponRepository
                .findByUserUsername(username, pageable)
                .map(UserCouponDto::fromEntity);
    }

    public Page<UserCouponDto> getAvailableUserCoupons(String username, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        return userCouponRepository
                .findByUserUsernameAndIsUsedFalseAndExpireAtAfterAndCoupon_ValidFromBeforeAndCoupon_ValidToAfter(
                        username,
                        now, // expireAtAfter 비교
                        now, // validFromBefore 비교
                        now, // validToAfter 비교
                        pageable
                )
                .map(UserCouponDto::fromEntity);
    }

    @Transactional
    public UserCouponDto issueCouponToUser(org.springframework.security.core.userdetails.User user, Coupon coupon, String source) {
        coupon = couponRepository.findById(coupon.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

        if (!coupon.getAllowDuplicate()) {
            boolean exists = userCouponRepository.existsByUserUsernameAndCouponId(user.getUsername(), coupon.getId());
            if (exists) throw new IllegalStateException("이미 발급된 쿠폰입니다.");
        }

        System.out.println(">>> DEBUG 발급 시도 쿠폰 정보");
System.out.println("couponId = " + coupon.getId());
System.out.println("maxIssuance = " + coupon.getMaxIssuance());
System.out.println("issuedCount = " + coupon.getIssuedCount());


 if (coupon.getMaxIssuance() != null && coupon.getMaxIssuance() > 0) {
    if (coupon.getIssuedCount() >= coupon.getMaxIssuance()) {
        throw new IllegalStateException("쿠폰 발급 수량이 모두 소진되었습니다.");
    }
}




        LocalDateTime expireAt;
        if (coupon.getValidPeriodAfterDownload() != null) {
            expireAt = LocalDateTime.now().plusDays(coupon.getValidPeriodAfterDownload());
        } else {
            expireAt = LocalDateTime.now().plusDays(7); // 기본값 7일
        }

        UserCoupon userCoupon = UserCoupon.builder()
                .user(userRepository.findByUsername(user.getUsername()).orElseThrow())
                .coupon(coupon)
                .issuedAt(LocalDateTime.now())
                .expireAt(expireAt)
                .isUsed(false)
                .issuedSource(source)
                .build();

        coupon.setIssuedCount(Optional.ofNullable(coupon.getIssuedCount()).orElse(0) + 1);
        couponRepository.save(coupon);

        return UserCouponDto.fromEntity(userCouponRepository.save(userCoupon));
    }

    // ✅ 쿠폰 생성 메서드 추가
@Transactional
public CouponDto createCoupon(CouponDto dto, org.springframework.security.core.userdetails.User loginUser) {
    // 로그인한 사용자 엔티티 조회
    User user = userRepository.findByUsername(loginUser.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    Coupon.CouponBuilder builder = Coupon.builder()
            .name(dto.getName())
            .couponCode(dto.getCouponCode())
            .discountAmount(dto.getDiscountAmount())
            .discountPercent(dto.getDiscountPercent())
            .validFrom(dto.getValidFrom())
            .validTo(dto.getValidTo())
            .validPeriodAfterDownload(dto.getValidPeriodAfterDownload())
            .maxIssuance(dto.getMaxIssuance() != null && dto.getMaxIssuance() == 0 ? null : dto.getMaxIssuance())
            .issuedCount(dto.getIssuedCount() != null ? dto.getIssuedCount() : 0)
            .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
            .allowDuplicate(dto.getAllowDuplicate() != null ? dto.getAllowDuplicate() : false)
            .issuanceType(dto.getIssuanceType());

   

    Coupon coupon = builder.build();
    Coupon saved = couponRepository.save(coupon);
    return CouponDto.fromEntity(saved);
}




    @Transactional
    public void issueWelcomeCoupon(org.springframework.security.core.userdetails.User user) {
        LocalDateTime now = LocalDateTime.now();

        // AUTO 타입 + 현재 유효한 쿠폰 하나 가져오기
        Optional<Coupon> welcomeCoupon = couponRepository
                .findFirstByIssuanceTypeAndIsActiveTrueAndValidFromBeforeAndValidToAfter(
                        Coupon.IssuanceType.AUTO, now, now
                );

        if (welcomeCoupon.isEmpty()) return; // 없으면 발급 안 함

        Coupon coupon = welcomeCoupon.get();

        // 중복 발급 방지 (allowDuplicate = false 일 경우)
        if (!coupon.getAllowDuplicate()) {
            boolean exists = userCouponRepository.existsByUserUsernameAndCouponId(user.getUsername(), coupon.getId());
            if (exists) return; // 이미 있으면 스킵
        }

        issueCouponToUser(user, coupon, "AUTO");
    }
}
