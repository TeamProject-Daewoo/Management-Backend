package com.example.backend.setPrice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomPriceDetailDto {
    private Long roomId;
    private String roomTitle;
    private Integer price;
}