package com.example.backend.hotel_intro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelIntroDTO {
    private Long id;
    private String contentid;
    private String checkintime;
    private String checkouttime;
    private Integer accomcountlodging;
    private Integer roomcount;
    private String roomtype;
    private String scalelodging;
    private String subfacility;
    private String parkinglodging;
    private Integer sauna;            // ← int로 맞춤 (DB도 int)
    private String reservationlodging;
    private String reservationurl;
}
