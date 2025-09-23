package com.example.backend.hotel;

import com.example.backend.hotel_intro.HotelIntroDTO;
import com.example.backend.room.RoomDTO;
import lombok.Data;
import java.util.List;

@Data
public class HotelRegisterRequest {

    // 호텔 기본 정보
    private HotelDTO hotel;

    // 호텔 소개 정보
    private HotelIntroDTO intro;

    // 호텔 객실 정보 (여러 개 가능)
    private List<RoomDTO> rooms;
}
