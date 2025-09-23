package com.example.backend.hotel_intro;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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
  private String sauna;
  private String fitness;
  private String barbecue;
  private String beverage;
  private String bicycle;
  private String reservationlodging;
  private String reservationurl;

  // ✅ Entity → DTO 변환
  public static HotelIntroDTO from(HotelIntro i) {
    return HotelIntroDTO.builder()
        .id(i.getId())
        .contentid(i.getContentid())
        .checkintime(i.getCheckintime())
        .checkouttime(i.getCheckouttime())
        .accomcountlodging(i.getAccomcountlodging())
        .roomcount(i.getRoomcount())
        .roomtype(i.getRoomtype())
        .scalelodging(i.getScalelodging())
        .subfacility(i.getSubfacility())
        .parkinglodging(i.getParkinglodging())
        .sauna(i.getSauna())
        .fitness(i.getFitness())
        .barbecue(i.getBarbecue())
        .beverage(i.getBeverage())
        .bicycle(i.getBicycle())
        .reservationlodging(i.getReservationlodging())
        .reservationurl(i.getReservationurl())
        .build();
  }

  // ✅ DTO → Entity 변환 (contentid 외부에서 주입받음)
  public HotelIntro toEntity(String contentid) {
    HotelIntro i = new HotelIntro();
    i.setContentid(contentid);
    i.setCheckintime(checkintime);
    i.setCheckouttime(checkouttime);
    i.setAccomcountlodging(accomcountlodging);
    i.setRoomcount(roomcount);
    i.setRoomtype(roomtype);
    i.setScalelodging(scalelodging);
    i.setSubfacility(subfacility);
    i.setParkinglodging(parkinglodging);
    i.setSauna(sauna);
    i.setFitness(fitness);
    i.setBarbecue(barbecue);
    i.setBeverage(beverage);
    i.setBicycle(bicycle);
    i.setReservationlodging(reservationlodging);
    i.setReservationurl(reservationurl);
    return i;
  }
}
