// HotelAdminController.java
package com.example.backend.hotel;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.hotel_intro.HotelIntroDTO;
import com.example.backend.hotel_intro.HotelIntroUpdateRequest;
import com.example.backend.payment.PaymentDTO;
import com.example.backend.reservation.BulkRequest;
import com.example.backend.reservation.ReservationDTO;
import com.example.backend.room.RoomDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/business")
public class HotelAdminController {

  private final HotelAdminService svc;

  // ▶ 소유 호텔 목록
  @GetMapping("/hotels")
  public List<HotelDTO> listMyHotels() {
    return svc.listMyHotels();
  }

  // ▶ 기본 정보
  @GetMapping("/hotel")
  public HotelDTO getHotel(@RequestParam(value = "contentid", required = false) String contentid) {
    return svc.getHotelBasic(contentid);
  }

  @PutMapping("/hotel")
  public HotelDTO updateHotel(@RequestParam(value = "contentid", required = false) String contentid,
                              @RequestBody HotelUpdateRequest req) {
    return svc.updateHotelBasic(contentid, req);
  }

  // ▶ 인트로
  @GetMapping("/hotel/intro")
  public HotelIntroDTO getIntro(@RequestParam(value = "contentid", required = false) String contentid) {
    return svc.getHotelIntro(contentid);
  }

  @PutMapping("/hotel/intro")
  public HotelIntroDTO upsertIntro(@RequestParam(value = "contentid", required = false) String contentid,
                                   @RequestBody HotelIntroUpdateRequest req) {
    return svc.upsertHotelIntro(contentid, req);
  }

  // ▶ 객실
  @GetMapping("/rooms")
  public List<RoomDTO> rooms(@RequestParam(value = "contentid", required = false) String contentid) {
    return svc.getRooms(contentid);
  }

  @PostMapping("/rooms")
  public RoomDTO createRoom(@RequestParam(value = "contentid", required = false) String contentid,
                            @RequestBody RoomDTO dto) {
    return svc.createRoom(contentid, dto);
  }

  @PutMapping("/rooms/{id}")
  public RoomDTO updateRoom(@RequestParam(value = "contentid", required = false) String contentid,
                            @PathVariable Long id, @RequestBody RoomDTO dto) {
    return svc.updateRoom(contentid, id, dto);
  }

  @DeleteMapping("/rooms/{id}")
  public void deleteRoom(@RequestParam(value = "contentid", required = false) String contentid,
                         @PathVariable Long id) {
    svc.deleteRoom(contentid, id);
  }

  // ▶ 예약/결제
  @GetMapping("/reservations")
  public List<ReservationDTO> reservations(@RequestParam(value = "contentid", required = false) String contentid) {
    return svc.getReservations(contentid);
  }

  @GetMapping("/payments")
  public List<PaymentDTO> payments(@RequestParam(value = "contentid", required = false) String contentid) {
    return svc.getPaymentsForHotel(contentid);
  }

  // ▶ 예약 일괄 작업
  @PostMapping("/reservations/bulk")
  public ResponseEntity<Void> bulkReservations(
          @RequestParam(required = false) String contentid,
          @RequestBody BulkRequest req) {
      svc.processBulkReservations(contentid, req.getIds(), req.getAction());
      return ResponseEntity.ok().build();
  }

  @PutMapping("/reservations/{id}/status")
  public ReservationDTO updateReservationStatus(
      @PathVariable Long id,
      @RequestParam String status
  ) {
      return svc.updateReservationStatus(id, status);
  }

  @PutMapping("/payments/{id}/status")
public ResponseEntity<Void> updatePaymentStatus(
        @PathVariable Long id,
        @RequestParam String status) {
    svc.updatePaymentStatus(id, status);
    return ResponseEntity.ok().build();
}

 @PutMapping("/reservations/{id}")
    public ReservationDTO updateReservation(
            @PathVariable Long id,
            @RequestBody ReservationDTO dto
    ) {
        return svc.updateReservation(id, dto); // 서비스에서 통합 수정 처리
    }

}
