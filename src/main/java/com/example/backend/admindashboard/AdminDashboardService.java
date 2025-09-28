package com.example.backend.admindashboard;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.backend.hotel.HotelRepository;
import com.example.backend.reservation.ReservationRepository;
import com.example.backend.room.RoomRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

  private final HotelRepository hotelRepository;
  private final ReservationRepository reservationRepository;
  private final RoomRepository roomRepository;

  public List<AdminDashboardResponseDto> getAllHotel(String businessNumber) {
    //호텔 contentId만 추출
    List<AdminDashboardHotelsDto> hotelDtos = businessNumber == null ? hotelRepository.findAdminDashboard() : hotelRepository.findAdminDashboardByBusinessNumber(businessNumber);
    List<String> hotelIds = hotelDtos.stream().map(AdminDashboardHotelsDto::getContentId).collect(Collectors.toList());
    
    List<AdminDashboardRoomsDto> rooms = roomRepository.getRoomsByHotelIds(hotelIds);
    List<AdminDashboardReservationDto> reservations = reservationRepository.getReservationsByHotelIds(hotelIds);

    Map<String, List<AdminDashboardRoomsDto>> roomMap = rooms.stream()
        .collect(Collectors.groupingBy(AdminDashboardRoomsDto::getHotelContentid));
    Map<String, List<AdminDashboardReservationDto>> reservationMap = reservations.stream()
        .collect(Collectors.groupingBy(AdminDashboardReservationDto::getContentId));

    return hotelDtos.stream()
        .map(h -> {
            AdminDashboardResponseDto finalDto = new AdminDashboardResponseDto(h);
            finalDto.setRooms(roomMap.getOrDefault(h.getContentId(), Collections.emptyList()));
            finalDto.setReservations(reservationMap.getOrDefault(h.getContentId(), Collections.emptyList()));
            return finalDto;
        })
        .collect(Collectors.toList());
  }

}
