package com.example.backend.hotel;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.backend.hotel_intro.*;
import com.example.backend.payment.PaymentDTO;
import com.example.backend.reservation.ReservationDTO;
import com.example.backend.room.RoomDTO;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/business")
public class HotelAdminController {

  private final HotelAdminService svc;

  @GetMapping("/hotel")
  public HotelDTO getHotel() { return svc.getHotelBasic(); }

  @PutMapping("/hotel")
  public HotelDTO updateHotel(@RequestBody HotelUpdateRequest req) { return svc.updateHotelBasic(req); }

  @GetMapping("/hotel/intro")
  public HotelIntroDTO getIntro() { return svc.getHotelIntro(); }

  @PutMapping("/hotel/intro")
  public HotelIntroDTO upsertIntro(@RequestBody HotelIntroUpdateRequest req) { return svc.upsertHotelIntro(req); }

  @GetMapping("/rooms")
  public List<RoomDTO> rooms() { return svc.getRooms(); }

  @PostMapping("/rooms")
  public RoomDTO createRoom(@RequestBody RoomDTO dto) { return svc.createRoom(dto); }

  @PutMapping("/rooms/{id}")
  public RoomDTO updateRoom(@PathVariable Long id, @RequestBody RoomDTO dto) { return svc.updateRoom(id, dto); }

  @DeleteMapping("/rooms/{id}")
  public void deleteRoom(@PathVariable Long id) { svc.deleteRoom(id); }

  @GetMapping("/reservations")
  public List<ReservationDTO> reservations() { return svc.getReservations(); }

  @GetMapping("/payments")
  public List<PaymentDTO> payments() { return svc.getPaymentsForHotel(); }
}
