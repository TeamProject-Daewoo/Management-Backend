package com.example.backend.hotel_intro;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "hotel_intro")
@Getter
@Setter
public class HotelIntro {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer accomcountlodging;

  private Integer roomcount;

  private String barbecue;

  private String beauty;

  private String beverage;

  private String bicycle;

  private String campfire;

  private String checkintime;

  private String checkouttime;

  private String chkcooking;

  // 문자열 contentid (hotels.contentid)
  private String contentid;

  private String fitness;

  private String foodplace;

  private String infocenterlodging;

  private String karaoke;

  private String parkinglodging;

  private String publicbath;

  private String publicpc;

  private String reservationlodging;

  private String reservationurl;

  private String roomtype;

  private String sauna;

  private String scalelodging;

  private String seminar;

  private String sports;

  private String subfacility;
}
