package com.example.backend.hotel_intro;

import lombok.Data;

@Data
public class HotelIntroUpdateRequest {
  private String checkintime;
  private String checkouttime;
  private Integer accomcountlodging;
  private Integer roomcount;
  private String roomtype;
  private String scalelodging;
  private String subfacility;
  private String parkinglodging;
  private Integer sauna;
  private String reservationlodging;
  private String reservationurl;
}
