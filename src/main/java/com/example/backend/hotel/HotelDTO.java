package com.example.backend.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class HotelDTO {
  private String contentid;  // PK
  private String title;
  private String addr1;
  private String tel;
  private String firstimage;
  private String mapx;
  private String mapy;
}
