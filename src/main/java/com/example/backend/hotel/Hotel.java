package com.example.backend.hotel;

import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;

@Entity @Table(name="hotels")
@Getter @Setter
public class Hotel {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String addr1;
  @Column(name="area_code") private String areaCode;
  private String category;
  private String contentid;     // varchar
  private String firstimage;
  private String mapx;
  private String mapy;
  @Column(name="sigungu_code") private String sigunguCode;
  private String tel;
  private String title;
}
