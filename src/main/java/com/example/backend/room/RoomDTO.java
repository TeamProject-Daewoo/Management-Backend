package com.example.backend.room;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomDTO {
  private Long id;
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
  private String roomimg1;
  private String roomimg2;
  private String roomimg3;
  private String roomimg4;
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

  private String thumb;

  public static RoomDTO from(Room r) {
    return RoomDTO.builder()
        .id(r.getId())
        .roomcode(r.getRoomcode())
        .roomtitle(r.getRoomtitle())
        .roombasecount(r.getRoombasecount())
        .roommaxcount(r.getRoommaxcount())
        .roomcount(r.getRoomcount())
        .roomoffseasonminfee1(r.getRoomoffseasonminfee1())
        .roomoffseasonminfee2(r.getRoomoffseasonminfee2())
        .roompeakseasonminfee1(r.getRoompeakseasonminfee1())
        .roompeakseasonminfee2(r.getRoompeakseasonminfee2())
        .roomsize1(r.getRoomsize1())
        .roomsize2(r.getRoomsize2())
        .roomintro(r.getRoomintro())
        // 이미지
        .roomimg1(r.getRoomimg1())
        .roomimg2(r.getRoomimg2())
        .roomimg3(r.getRoomimg3())
        .roomimg4(r.getRoomimg4())
        .roomimg5(r.getRoomimg5())
        // 옵션
        .roomaircondition(r.getRoomaircondition())
        .roombath(r.getRoombath())
        .roombathfacility(r.getRoombathfacility())
        .roomcable(r.getRoomcable())
        .roomcook(r.getRoomcook())
        .roomhairdryer(r.getRoomhairdryer())
        .roomhometheater(r.getRoomhometheater())
        .roominternet(r.getRoominternet())
        .roompc(r.getRoompc())
        .roomrefrigerator(r.getRoomrefrigerator())
        .roomsofa(r.getRoomsofa())
        .roomtable(r.getRoomtable())
        .roomtoiletries(r.getRoomtoiletries())
        .roomtv(r.getRoomtv())
        // 대표 이미지
        .thumb(firstNonBlank(r.getRoomimg1(), r.getRoomimg2(), r.getRoomimg3(), r.getRoomimg4(), r.getRoomimg5()))
        .build();
  }

  public Room toEntity(String contentid) {
    Room r = new Room();
    r.setContentid(contentid);
    r.setRoomcode(roomcode);
    r.setRoomtitle(roomtitle);
    r.setRoombasecount(roombasecount);
    r.setRoommaxcount(roommaxcount);
    r.setRoomcount(roomcount);
    r.setRoomoffseasonminfee1(roomoffseasonminfee1);
    r.setRoomoffseasonminfee2(roomoffseasonminfee2);
    r.setRoompeakseasonminfee1(roompeakseasonminfee1);
    r.setRoompeakseasonminfee2(roompeakseasonminfee2);
    r.setRoomsize1(roomsize1);
    r.setRoomsize2(roomsize2);
    r.setRoomintro(roomintro);
    r.setRoomimg1(roomimg1);
    r.setRoomimg2(roomimg2);
    r.setRoomimg3(roomimg3);
    r.setRoomimg4(roomimg4);
    r.setRoomimg5(roomimg5);
    r.setRoomaircondition(roomaircondition);
    r.setRoombath(roombath);
    r.setRoombathfacility(roombathfacility);
    r.setRoomcable(roomcable);
    r.setRoomcook(roomcook);
    r.setRoomhairdryer(roomhairdryer);
    r.setRoomhometheater(roomhometheater);
    r.setRoominternet(roominternet);
    r.setRoompc(roompc);
    r.setRoomrefrigerator(roomrefrigerator);
    r.setRoomsofa(roomsofa);
    r.setRoomtable(roomtable);
    r.setRoomtoiletries(roomtoiletries);
    r.setRoomtv(roomtv);
    return r;
  }

  private static String firstNonBlank(String... arr) {
    if (arr == null)
      return null;
    for (String s : arr) {
      if (s != null && !s.isBlank())
        return s.trim();
    }
    return null;
  }
}
