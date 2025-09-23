package com.example.backend.setPrice;

import java.time.LocalDate;
import java.util.Map;

import lombok.Getter;

@Getter
public class PriceOverrideRequestDTO {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Map<Long, Integer> priceOverrides; // Key: roomId, Value: price
    private String hotelContentId;
}
