// com.example.backend.hotel_intro.HotelIntroDTO
package com.example.backend.hotel_intro;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class HotelIntroDTO {
  private Long id; private String contentid;
  private String checkintime; private String checkouttime;
  private Integer accomcountlodging; private Integer roomcount;
  private String roomtype; private String scalelodging; private String subfacility;
  private String parkinglodging; private String sauna; private String fitness;
  private String barbecue; private String beverage; private String bicycle;
  private String reservationlodging; private String reservationurl;
}
