package com.example.backend.admindashboard;

import java.util.List;

import com.example.backend.hotel.Hotel;

import lombok.Data;

@Data
public class AdminDashboardResponseDto {
    private String contentId;
    private String title;
    private String area;
    private String businessNumber;

    private List<AdminDashboardRoomsDto> rooms;
    private List<AdminDashboardReservationDto> reservations;
    AdminDashboardResponseDto(AdminDashboardHotelsDto hotel) {
        this.contentId = hotel.getContentId();
        this.title = hotel.getTitle();
        this.area = hotel.getArea();
        this.businessNumber = hotel.getBusinessNumber();
    }
}