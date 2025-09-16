package com.example.backend.hotel_intro;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name="hotel_intro")
@Getter @Setter
public class HotelIntro {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer accomcountlodging;
  private Integer roomcount;

  private Integer sauna;
  private Integer seminar;
  private Integer sports;

  private String checkintime;
  private String checkouttime;
  private String chkcooking;

  private String contentid;          // FK -> hotels.contentid (varchar)

  private String contenttypeid;
  private String foodplace;
  private String infocenterlodging;
  private String parkinglodging;
  private String reservationlodging;
  private String reservationurl;
  private String roomtype;
  private String scalelodging;
  private String subfacility;

  private String pickup;
  private String refundregulation;
}
