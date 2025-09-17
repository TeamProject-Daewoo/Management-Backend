// com.example.backend.hotel_intro.HotelIntroUpdateRequest
package com.example.backend.hotel_intro;
import lombok.Data;
@Data
public class HotelIntroUpdateRequest {
  private String checkintime; private String checkouttime;
  private Integer accomcountlodging; private Integer roomcount;
  private String roomtype; private String scalelodging; private String subfacility;
  private String parkinglodging; private String sauna; private String fitness;
  private String barbecue; private String beverage; private String bicycle;
  private String reservationlodging; private String reservationurl;
}
