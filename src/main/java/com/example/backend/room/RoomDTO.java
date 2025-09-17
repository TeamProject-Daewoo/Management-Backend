// com.example.backend.room.RoomDTO
package com.example.backend.room;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class RoomDTO {
  private Long id; private String roomcode; private String roomtitle;
  private Integer roombasecount; private Integer roommaxcount;
  private Integer roomoffseasonminfee1; private Integer roompeakseasonminfee1;
}
