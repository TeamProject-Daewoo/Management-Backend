package com.example.backend.setPrice;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialPriceGroupDto {
    private Long id; // 대표 ID (예: 첫 번째 override의 ID)
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<RoomPriceDetailDto> roomPrices;
}
