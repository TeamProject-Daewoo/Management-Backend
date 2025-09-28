package com.example.backend.admindashboard;

import com.example.backend.room.Room;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminDashboardRoomsDto {
    private Long roomId;
    private String roomName; 
    private String hotelContentid; 
    private Integer roomcount;

    public AdminDashboardRoomsDto(Room room) {
        this.roomId = room.getId();
        this.roomName = room.getRoomtitle();
        this.hotelContentid = room.getContentid();
        this.roomcount = room.getRoomcount();
    }
}
