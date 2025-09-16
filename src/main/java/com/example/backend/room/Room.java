package com.example.backend.room;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name="rooms")
@Getter @Setter
public class Room {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer roombasecount;
  private Integer roomcount;
  private Integer roommaxcount;
  private Integer roomoffseasonminfee1;
  private Integer roomoffseasonminfee2;
  private Integer roompeakseasonminfee1;
  private Integer roompeakseasonminfee2;

  private String contentid;   // FK(varchar)
  private String roomcode;
  private String roomtitle;

  // 기타 칼럼 생략 (필요시 추가)
}
