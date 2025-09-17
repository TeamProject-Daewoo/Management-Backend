// com.example.backend.hotel.HotelDTO
package com.example.backend.hotel;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class HotelDTO {
  private Long id; private String contentid; private String title;
  private String addr1; private String tel; private String firstimage;
  private String mapx; private String mapy;
}
