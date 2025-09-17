package com.example.backend.hotel;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.hotel_intro.HotelIntroDTO;
import com.example.backend.hotel_intro.HotelIntroUpdateRequest;
import com.example.backend.payment.PaymentDTO;
import com.example.backend.reservation.ReservationDTO;
import com.example.backend.room.RoomDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/business")
public class HotelAdminController {

  private final HotelAdminService svc;

  @GetMapping("/hotel")
  public HotelDTO getHotel() {
    return svc.getHotelBasic();
  }

  @PutMapping("/hotel")
  public HotelDTO updateHotel(@RequestBody HotelUpdateRequest req) {
    return svc.updateHotelBasic(req);
  }

  @GetMapping("/hotel/intro")
  public HotelIntroDTO getIntro() {
    return svc.getHotelIntro();
  }

  @PutMapping("/hotel/intro")
  public HotelIntroDTO upsertIntro(@RequestBody HotelIntroUpdateRequest req) {
    return svc.upsertHotelIntro(req);
  }

  @GetMapping("/rooms")
  public List<RoomDTO> rooms() {
    return svc.getRooms();
  }

  @PostMapping("/rooms")
  public RoomDTO createRoom(@RequestBody RoomDTO dto) {
    return svc.createRoom(dto);
  }

  @PutMapping("/rooms/{id}")
  public RoomDTO updateRoom(@PathVariable Long id, @RequestBody RoomDTO dto) {
    return svc.updateRoom(id, dto);
  }

  @DeleteMapping("/rooms/{id}")
  public void deleteRoom(@PathVariable Long id) {
    svc.deleteRoom(id);
  }

  @GetMapping("/reservations")
  public List<ReservationDTO> reservations() {
    return svc.getReservations();
  }

  @GetMapping("/payments")
  public List<PaymentDTO> payments() {
    return svc.getPaymentsForHotel();
  }
}
