package com.example.backend.hotel;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.hotel_intro.*;
import com.example.backend.payment.*;
import com.example.backend.reservation.*;
import com.example.backend.room.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HotelAdminService {

  private final HotelRepository hotelRepo;
  private final HotelIntroRepository introRepo;
  private final RoomRepository roomRepo;
  private final ReservationRepository reservationRepo;
  private final PaymentRepository paymentRepo;

  @Value("${hotel.contentid}")   // 예: 2465071
  private String TARGET_CONTENTID;

  @Transactional(readOnly = true)
  public Hotel getTargetHotelOrThrow() {
    return hotelRepo.findById(TARGET_CONTENTID)
        .orElseThrow(() -> new IllegalStateException("호텔(contentid=" + TARGET_CONTENTID + ")이 없습니다."));
  }

  // -------- Hotel Basic --------
  @Transactional(readOnly = true)
  public HotelDTO getHotelBasic() {
    Hotel h = getTargetHotelOrThrow();
    return new HotelDTO(
        h.getContentid(), h.getTitle(), h.getAddr1(), h.getTel(),
        h.getFirstimage(), h.getMapx(), h.getMapy()
    );
  }

  @Transactional
  public HotelDTO updateHotelBasic(HotelUpdateRequest req) {
    Hotel h = getTargetHotelOrThrow();
    if (req.getTitle() != null) h.setTitle(req.getTitle());
    if (req.getAddr1() != null) h.setAddr1(req.getAddr1());
    if (req.getTel() != null) h.setTel(req.getTel());
    if (req.getFirstimage() != null) h.setFirstimage(req.getFirstimage());
    if (req.getMapx() != null) h.setMapx(req.getMapx());
    if (req.getMapy() != null) h.setMapy(req.getMapy());
    hotelRepo.save(h);
    return getHotelBasic();
  }

  // -------- Hotel Intro --------
  @Transactional(readOnly = true)
  public HotelIntroDTO getHotelIntro() {
    HotelIntro i = introRepo.findTopByContentidOrderByIdDesc(TARGET_CONTENTID).orElse(null);
    if (i == null) return null;
    return new HotelIntroDTO(
        i.getId(), i.getContentid(),
        i.getCheckintime(), i.getCheckouttime(),
        i.getAccomcountlodging(), i.getRoomcount(),
        i.getRoomtype(), i.getScalelodging(),
        i.getSubfacility(), i.getParkinglodging(),
        i.getSauna(),
        i.getReservationlodging(), i.getReservationurl()
    );
  }

  @Transactional
  public HotelIntroDTO upsertHotelIntro(HotelIntroUpdateRequest r) {
    HotelIntro i = introRepo.findTopByContentidOrderByIdDesc(TARGET_CONTENTID)
        .orElseGet(() -> { HotelIntro x = new HotelIntro(); x.setContentid(TARGET_CONTENTID); return x; });

    i.setCheckintime(r.getCheckintime());
    i.setCheckouttime(r.getCheckouttime());
    i.setAccomcountlodging(r.getAccomcountlodging());
    i.setRoomcount(r.getRoomcount());
    i.setRoomtype(r.getRoomtype());
    i.setScalelodging(r.getScalelodging());
    i.setSubfacility(r.getSubfacility());
    i.setParkinglodging(r.getParkinglodging());
    i.setSauna(r.getSauna());
    i.setReservationlodging(r.getReservationlodging());
    i.setReservationurl(r.getReservationurl());

    introRepo.save(i);
    return getHotelIntro();
  }

  // -------- Rooms --------
  @Transactional(readOnly = true)
  public List<RoomDTO> getRooms() {
    return roomRepo.findByContentid(TARGET_CONTENTID).stream()
        .map(r -> new RoomDTO(
            r.getId(), r.getRoomcode(), r.getRoomtitle(),
            r.getRoombasecount(), r.getRoommaxcount(),
            r.getRoomoffseasonminfee1(), r.getRoompeakseasonminfee1()
        ))
        .toList();
  }

  @Transactional
  public RoomDTO createRoom(RoomDTO dto) {
    Room r = new Room();
    r.setContentid(TARGET_CONTENTID);
    r.setRoomcode(dto.getRoomcode());
    r.setRoomtitle(dto.getRoomtitle());
    r.setRoombasecount(dto.getRoombasecount());
    r.setRoommaxcount(dto.getRoommaxcount());
    r.setRoomoffseasonminfee1(dto.getRoomoffseasonminfee1());
    r.setRoompeakseasonminfee1(dto.getRoompeakseasonminfee1());
    roomRepo.save(r);

    return new RoomDTO(
        r.getId(), r.getRoomcode(), r.getRoomtitle(),
        r.getRoombasecount(), r.getRoommaxcount(),
        r.getRoomoffseasonminfee1(), r.getRoompeakseasonminfee1()
    );
  }

  @Transactional
  public RoomDTO updateRoom(Long roomId, RoomDTO dto) {
    Room r = roomRepo.findById(roomId).orElseThrow();
    if (dto.getRoomcode() != null) r.setRoomcode(dto.getRoomcode());
    if (dto.getRoomtitle() != null) r.setRoomtitle(dto.getRoomtitle());
    if (dto.getRoombasecount() != null) r.setRoombasecount(dto.getRoombasecount());
    if (dto.getRoommaxcount() != null) r.setRoommaxcount(dto.getRoommaxcount());
    if (dto.getRoomoffseasonminfee1() != null) r.setRoomoffseasonminfee1(dto.getRoomoffseasonminfee1());
    if (dto.getRoompeakseasonminfee1() != null) r.setRoompeakseasonminfee1(dto.getRoompeakseasonminfee1());
    roomRepo.save(r);

    return new RoomDTO(
        r.getId(), r.getRoomcode(), r.getRoomtitle(),
        r.getRoombasecount(), r.getRoommaxcount(),
        r.getRoomoffseasonminfee1(), r.getRoompeakseasonminfee1()
    );
  }

  @Transactional
  public void deleteRoom(Long roomId) {
    roomRepo.deleteById(roomId);
  }

  // -------- Reservations / Payments --------
  @Transactional(readOnly = true)
  public List<ReservationDTO> getReservations() {
    return reservationRepo.findByContentidOrderByReservationDateDesc(TARGET_CONTENTID)
        .stream()
        .map(r -> new ReservationDTO(
            r.getReservationId(),
            r.getUserName(),
            r.getUser()!=null ? r.getUser().getName() : null,   // bookerName
            r.getUser()!=null ? r.getUser().getEmail() : null,
            r.getUser()!=null ? r.getUser().getPhoneNumber() : null,
            r.getCheckInDate(), r.getCheckOutDate(),
            r.getRoomcode(), r.getStatus(), r.getTotalPrice(),
            r.getReservationDate()
        ))
        .toList();
  }

  @Transactional(readOnly = true)
  public List<PaymentDTO> getPaymentsForHotel() {
    // 1) 이 호텔의 예약 id 목록
    List<Long> resIds = reservationRepo.findByContentidOrderByReservationDateDesc(TARGET_CONTENTID)
        .stream().map(Reservation::getReservationId).toList();
    if (resIds.isEmpty()) return List.of();

    // 2) 결제 목록
    return paymentRepo.findByReservationReservationIdIn(resIds).stream()
        .map(p -> new PaymentDTO(
            p.getPaymentId(),
            p.getReservation()!=null ? p.getReservation().getReservationId() : null,
            p.getUserName(),
            p.getPaymentAmount(),
            p.getPaymentMethod(),
            p.getPaymentStatus(),
            p.getPaymentDate()
        ))
        .toList();
  }
}
