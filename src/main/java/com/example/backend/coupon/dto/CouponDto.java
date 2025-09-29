package com.example.backend.coupon.dto;

import com.example.backend.coupon.entity.Coupon;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponDto {

    private Long id;
    private String name;
    private Integer discountAmount;
    private Integer discountPercent;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Integer validPeriodAfterDownload;
    private Integer maxIssuance;
    private Integer issuedCount;
    private Boolean isActive;
    private Boolean allowDuplicate;
    private String couponCode;
    private Coupon.IssuanceType issuanceType;
    private String hotelId;

    public static CouponDto fromEntity(Coupon coupon) {
        return CouponDto.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .discountAmount(coupon.getDiscountAmount())
                .discountPercent(coupon.getDiscountPercent())
                .validFrom(coupon.getValidFrom())
                .validTo(coupon.getValidTo())
                .validPeriodAfterDownload(coupon.getValidPeriodAfterDownload())
                .maxIssuance(coupon.getMaxIssuance())
                .issuedCount(coupon.getIssuedCount())
                .isActive(coupon.getIsActive())
                .allowDuplicate(coupon.getAllowDuplicate())
                .couponCode(coupon.getCouponCode())
                .issuanceType(coupon.getIssuanceType())
                .build();
    }
}
