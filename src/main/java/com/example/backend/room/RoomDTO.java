package com.example.backend.room;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomDTO {
  private Long id;
  private String roomcode;
  private String roomtitle;
  private Integer roombasecount;
  private Integer roommaxcount;
  private Integer roomcount;
  private Integer roomoffseasonminfee1;
  private Integer roompeakseasonminfee1;

  // 이미지들
  private String roomimg1;
  private String roomimg2;
  private String roomimg3;
  private String roomimg4;
  private String roomimg5;
  
  // 옵션(편의시설)
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

  // 대표 이미지
  private String thumb;

  public static RoomDTO from(Room r) {
    return RoomDTO.builder()
        .id(r.getId())
        .roomcode(r.getRoomcode())
        .roomtitle(r.getRoomtitle())
        .roombasecount(r.getRoombasecount())
        .roommaxcount(r.getRoommaxcount())
        .roomoffseasonminfee1(r.getRoomoffseasonminfee1())
        .roompeakseasonminfee1(r.getRoompeakseasonminfee1())
        .roomcount(r.getRoomcount())
        // 이미지들
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
        // 대표 이미지(첫 유효 이미지)
        .thumb(firstNonBlank(
            r.getRoomimg1(), r.getRoomimg2(), r.getRoomimg3(), r.getRoomimg4(), r.getRoomimg5()
        ))
        .build();
  }

  /** 가용한 첫 문자열 반환 (null/빈문자열/공백은 무시) */
  private static String firstNonBlank(String... arr) {
    if (arr == null) return null;
    for (String s : arr) {
      if (s != null && !s.isBlank()) return s.trim();
    }
    return null;
  }
}
