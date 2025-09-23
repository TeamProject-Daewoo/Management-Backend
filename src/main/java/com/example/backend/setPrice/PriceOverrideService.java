package com.example.backend.setPrice;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.room.Room;
import com.example.backend.room.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PriceOverrideService {
    private final RoomPriceOverrideRepository overrideRepository;
    private final RoomRepository roomRepository; // 객실(Detail) Repository

    @Transactional
    public void createOrUpdatePriceOverrides(PriceOverrideRequestDTO requestDTO) {
        // DTO에서 받은 priceOverrides 맵을 순회
        requestDTO.getPriceOverrides().forEach((roomId, price) -> {
            // 해당 roomId로 객실 엔티티를 찾음
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid room ID: " + roomId));

            // RoomPriceOverride 객체 생성
            RoomPriceOverride override = RoomPriceOverride.builder()
                    .title(requestDTO.getTitle())
                    .startDate(requestDTO.getStartDate())
                    .endDate(requestDTO.getEndDate())
                    .price(price)
                    .room(room)
                    .build();
            
            overrideRepository.save(override);
        });
    }
}