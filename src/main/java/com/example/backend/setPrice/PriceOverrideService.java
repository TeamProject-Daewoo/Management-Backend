package com.example.backend.setPrice;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    	
    	 List<RoomPriceOverride> overlaps = overrideRepository
                 .findByRoom_ContentidAndEndDateGreaterThanEqualAndStartDateLessThanEqual(
                		 requestDTO.getHotelContentId(),
                		 requestDTO.getStartDate(),
                		 requestDTO.getEndDate()
                 );

         // 겹치는 데이터가 하나라도 있다면 예외를 발생시켜 저장을 중단!
         if (!overlaps.isEmpty()) {
             throw new IllegalStateException("선택하신 기간에 이미 다른 특별가가 설정되어 있습니다. 시작일: " 
                 + overlaps.get(0).getStartDate() + ", 종료일: " + overlaps.get(0).getEndDate());
         }
         
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
    
public List<SpecialPriceGroupDto> findSpecialPricesByContentId(String contentId) {
        
        // [수정] contentId로 바로 특별가 정보를 조회합니다.
	 List<RoomPriceOverride> overrides = overrideRepository.findByRoom_Contentid(contentId);

        // 이후 데이터 가공 및 DTO 변환 로직은 동일합니다.
        Map<String, List<RoomPriceOverride>> groupedByEvent = overrides.stream()
                .collect(Collectors.groupingBy(override -> 
                    override.getTitle() + "::" + override.getStartDate() + "::" + override.getEndDate()
                ));

        return groupedByEvent.values().stream()
                .map(eventOverrides -> {
                    RoomPriceOverride first = eventOverrides.get(0);
                    List<RoomPriceDetailDto> roomPrices = eventOverrides.stream()
                            .map(o -> new RoomPriceDetailDto(
                                    o.getRoom().getId(),
                                    o.getRoom().getRoomtitle(),
                                    o.getPrice()
                            ))
                            .collect(Collectors.toList());

                    return new SpecialPriceGroupDto(
                            first.getId(),
                            first.getTitle(),
                            first.getStartDate(),
                            first.getEndDate(),
                            roomPrices
                    );
                })
                .collect(Collectors.toList());
    }

@Transactional
public void deleteSpecialPriceGroup(String title, LocalDate startDate, LocalDate endDate, String contentid) {
    // Repository에 정의한 메서드를 호출하여 조건에 맞는 모든 데이터를 한번에 삭제
    overrideRepository.deleteByTitleAndStartDateAndEndDateAndRoom_Contentid(
        title, startDate, endDate, contentid
    );
}
}