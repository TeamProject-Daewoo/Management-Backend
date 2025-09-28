package com.example.backend.hotel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HotelDTO {
  private Long id;
  private String contentid;
  private String title;
  private String addr1;
  private String tel;
  private String firstimage;
  private String mapx;
  private String mapy;
  private Integer areaCode;
  private Integer sigunguCode;
  private String category;
  private String businessRegistrationNumber;

  // ✅ Entity → DTO 변환
  public static HotelDTO from(Hotel h) {
    return HotelDTO.builder()
        .id(h.getId())
        .contentid(h.getContentid())
        .title(h.getTitle())
        .addr1(h.getAddr1())
        .tel(h.getTel())
        .firstimage(h.getFirstimage())
        .mapx(h.getMapx())
        .mapy(h.getMapy())
        .areaCode(h.getAreaCode())
        .sigunguCode(h.getSigunguCode())
        .category(h.getCategory())
        .businessRegistrationNumber(h.getBusinessRegistrationNumber())
        .build();
  }

  // ✅ DTO → Entity 변환
  public Hotel toEntity() {
    Hotel h = new Hotel();
    h.setId(id);
    h.setContentid(contentid);
    h.setTitle(title);
    h.setAddr1(addr1);
    h.setTel(tel);
    h.setFirstimage(firstimage);
    h.setMapx(mapx);
    h.setMapy(mapy);
    h.setAreaCode(areaCode);
    h.setSigunguCode(sigunguCode);
    h.setCategory(category);
    h.setBusinessRegistrationNumber(businessRegistrationNumber);
    return h;
  }
}
