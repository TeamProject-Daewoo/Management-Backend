package com.example.backend.hotel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name="hotels")
@Getter @Setter
public class Hotel {

  @Id
  @Column(name="contentid")
  private String contentid;          // PK (varchar)

  private String addr1;

  @Column(name="area_code")
  private String areaCode;

  @Column(name="cat1")  private String cat1;
  @Column(name="cat2")  private String cat2;
  @Column(name="cat3")  private String cat3;

  private String contenttypeid;
  private String firstimage;
  private String firstimage2;

  private String mapx;
  private String mapy;

  @Column(name="sigungu_code")
  private String sigunguCode;

  private String tel;
  private String title;
}
