package com.example.backend.room;

import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;

@Entity @Table(name="rooms")
@Getter @Setter
public class Room {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer roombasecount; private Integer roomcount; private Integer roommaxcount;
  private Integer roomoffseasonminfee1; private Integer roomoffseasonminfee2;
  private Integer roompeakseasonminfee1; private Integer roompeakseasonminfee2;

  private String contentid; // 문자열
  private String roomaircondition; private String roombath; private String roombathfacility;
  private String roomcable; private String roomcode; private String roomcook;
  private String roomhairdryer; private String roomhometheater;
  private String roomimg1; private String roomimg2; private String roomimg3; private String roomimg4; private String roomimg5;
  private String roominternet; private String roomintro; private String roompc;
  private String roomrefrigerator; private String roomsize1; private String roomsize2;
  private String roomsofa; private String roomtable; private String roomtitle;
  private String roomtoiletries; private String roomtv;
}
