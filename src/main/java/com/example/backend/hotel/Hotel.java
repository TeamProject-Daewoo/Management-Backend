package com.example.backend.hotel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "HOTELS", indexes = {
    @Index(name = "idx_title", columnList = "title"),
    @Index(name = "idx_addr1", columnList = "addr1"),
    @Index(name = "idx_business_registration_number", columnList = "business_registration_number")
})
public class Hotel {

  // ✅ 스키마상 PK는 contentid(varchar)
  @Id
  @Column(name = "contentid", nullable = false, updatable = false)
  private String contentid;

  // 스키마에 존재하지만 PK 아님 (AUTO_INCREMENT 아님)
  @Column(name = "id")
  private Long id;

  @Column(name = "addr1")
  private String addr1;

  @Column(name = "area_code")
  private Integer areaCode;

  @Column(name = "category")
  private String category;

  @Column(name = "firstimage")
  private String firstimage;

  @Column(name = "mapx")
  private String mapx;

  @Column(name = "mapy")
  private String mapy;

  @Column(name = "sigungu_code")
  private Integer sigunguCode;

  @Column(name = "tel")
  private String tel;

  @Column(name = "title")
  private String title;

  @Column(name = "business_registration_number")
  private String businessRegistrationNumber;
}
