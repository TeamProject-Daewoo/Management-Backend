package com.example.backend.setPrice;

import java.time.LocalDate;
import lombok.Data;

@Data
public class DeleteSpecialPriceRequestDto {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String hotelContentId;
}