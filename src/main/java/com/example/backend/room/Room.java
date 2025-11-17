package com.example.backend.room;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "rooms", indexes = {
    @Index(name = "idx_contentid", columnList = "contentid")
})
@Getter
@Setter
@ToString
public class Room {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String contentid; // FK = hotels.contentid

  private String roomcode;
  private String roomtitle;

  private Integer roombasecount;
  private Integer roommaxcount;
  private Integer roomcount;

  private Integer roomoffseasonminfee1;
  private Integer roomoffseasonminfee2;
  private Integer roompeakseasonminfee1;
  private Integer roompeakseasonminfee2;

  private String roomsize1;
  private String roomsize2;
  private String roomintro;

  // 이미지
  @Column(name = "roomimg1", length = 512)
  private String roomimg1;
  @Column(name = "roomimg2", length = 512)
  private String roomimg2;
  @Column(name = "roomimg3", length = 512)
  private String roomimg3;
  @Column(name = "roomimg4", length = 512)
  private String roomimg4;
  @Column(name = "roomimg5", length = 512)
  private String roomimg5;

  // 옵션
  private String roomaircondition;
  private String roombath;
  private String roombathfacility;
  private String roomcable;
  private String roomcook;
  private String roomhairdryer;
  private String roomhometheater;
  private String roominternet;
  private String roompc;
  private String roomrefrigerator;
  private String roomsofa;
  private String roomtable;
  private String roomtoiletries;
  private String roomtv;
}
