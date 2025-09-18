// HotelAdminService.java
package com.example.backend.hotel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.authentication.Role;
import com.example.backend.authentication.User;
import com.example.backend.authentication.UserRepository;
import com.example.backend.hotel_intro.HotelIntro;
import com.example.backend.hotel_intro.HotelIntroDTO;
import com.example.backend.hotel_intro.HotelIntroRepository;
import com.example.backend.hotel_intro.HotelIntroUpdateRequest;
import com.example.backend.payment.PaymentDTO;
import com.example.backend.payment.PaymentRepository;
import com.example.backend.reservation.Reservation;
import com.example.backend.reservation.ReservationDTO;
import com.example.backend.reservation.ReservationRepository;
import com.example.backend.room.Room;
import com.example.backend.room.RoomDTO;
import com.example.backend.room.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HotelAdminService {

  private final HotelRepository hotelRepo;
  private final HotelIntroRepository introRepo;
  private final RoomRepository roomRepo;
  private final ReservationRepository reservationRepo;
  private final PaymentRepository paymentRepo;
  private final UserRepository userRepo;

  /** 현재 로그인 사용자 */
  private User currentUserOrThrow() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getName() == null) throw new IllegalStateException("인증 정보가 없습니다.");
    return userRepo.findByUsername(auth.getName())
        .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다. username=" + auth.getName()));
  }

  /** 현재 BUSINESS 사용자 검사 + BRN 반환 */
  private String currentBusinessNumberOrThrow() {
    User u = currentUserOrThrow();
    if (u.getRole() != Role.BUSINESS) throw new IllegalStateException("BUSINESS 권한 사용자만 접근 가능합니다.");
    String brn = u.getBusiness_registration_number();
    if (brn == null || brn.isBlank()) throw new IllegalStateException("사용자에 사업자번호가 없습니다.");
    return brn;
  }

  /** 현재 BUSINESS 사용자가 소유한 모든 호텔 목록 */
  @Transactional(readOnly = true)
  public List<Hotel> getBusinessHotelsOrThrow() {
    String brn = currentBusinessNumberOrThrow();
    List<Hotel> hotels = hotelRepo.findAllByBusinessRegistrationNumber(brn);
    if (hotels.isEmpty()) throw new IllegalStateException("사업자번호에 해당하는 호텔이 없습니다. brn=" + brn);
    return hotels;
  }

  /** contentid 파라미터 해석: 넘겨지면 소유 검증, 없으면 목록 첫 번째 선택 */
  @Transactional(readOnly = true)
  public Hotel resolveHotelForBusiness(String contentid) {
    List<Hotel> hotels = getBusinessHotelsOrThrow();
    if (contentid == null || contentid.isBlank()) {
      return hotels.get(0); // 기본 선택 정책: 첫 호텔
    }
    return hotels.stream()
        .filter(h -> contentid.equals(h.getContentid()))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("해당 contentid의 호텔이 이 사업자 소유가 아닙니다: " + contentid));
  }

  // ====== Public APIs ======

  // 목록 조회
  @Transactional(readOnly = true)
  public List<HotelDTO> listMyHotels() {
    return getBusinessHotelsOrThrow().stream()
        .map(h -> new HotelDTO(
            h.getId(), h.getContentid(), h.getTitle(), h.getAddr1(),
            h.getTel(), h.getFirstimage(), h.getMapx(), h.getMapy()
        )).toList();
  }

  // ---------- Hotel Basic ----------
  @Transactional(readOnly = true)
  public HotelDTO getHotelBasic(String contentid) {
    Hotel h = resolveHotelForBusiness(contentid);
    return new HotelDTO(
        h.getId(), h.getContentid(), h.getTitle(), h.getAddr1(),
        h.getTel(), h.getFirstimage(), h.getMapx(), h.getMapy()
    );
  }

  @Transactional
  public HotelDTO updateHotelBasic(String contentid, HotelUpdateRequest req) {
    Hotel h = resolveHotelForBusiness(contentid);
    if (req.getTitle() != null) h.setTitle(req.getTitle());
    if (req.getAddr1() != null) h.setAddr1(req.getAddr1());
    if (req.getTel() != null) h.setTel(req.getTel());
    if (req.getFirstimage() != null) h.setFirstimage(req.getFirstimage());
    if (req.getMapx() != null) h.setMapx(req.getMapx());
    if (req.getMapy() != null) h.setMapy(req.getMapy());
    hotelRepo.save(h);
    return getHotelBasic(contentid);
  }

  // ---------- Hotel Intro ----------
  @Transactional(readOnly = true)
  public HotelIntroDTO getHotelIntro(String contentid) {
    String cid = resolveHotelForBusiness(contentid).getContentid();
    HotelIntro i = introRepo.findTopByContentidOrderByIdDesc(cid).orElse(null);
    if (i == null) return null;
    return new HotelIntroDTO(
        i.getId(), i.getContentid(), i.getCheckintime(), i.getCheckouttime(),
        i.getAccomcountlodging(), i.getRoomcount(), i.getRoomtype(), i.getScalelodging(),
        i.getSubfacility(), i.getParkinglodging(), i.getSauna(), i.getFitness(),
        i.getBarbecue(), i.getBeverage(), i.getBicycle(),
        i.getReservationlodging(), i.getReservationurl()
    );
  }

  @Transactional
  public HotelIntroDTO upsertHotelIntro(String contentid, HotelIntroUpdateRequest r) {
    String cid = resolveHotelForBusiness(contentid).getContentid();
    HotelIntro i = introRepo.findTopByContentidOrderByIdDesc(cid)
        .orElseGet(() -> { HotelIntro x = new HotelIntro(); x.setContentid(cid); return x; });

    i.setCheckintime(r.getCheckintime());
    i.setCheckouttime(r.getCheckouttime());
    i.setAccomcountlodging(r.getAccomcountlodging());
    i.setRoomcount(r.getRoomcount());
    i.setRoomtype(r.getRoomtype());
    i.setScalelodging(r.getScalelodging());
    i.setSubfacility(r.getSubfacility());
    i.setParkinglodging(r.getParkinglodging());
    i.setSauna(r.getSauna());
    i.setFitness(r.getFitness());
    i.setBarbecue(r.getBarbecue());
    i.setBeverage(r.getBeverage());
    i.setBicycle(r.getBicycle());
    i.setReservationlodging(r.getReservationlodging());
    i.setReservationurl(r.getReservationurl());
    introRepo.save(i);

    return getHotelIntro(cid);
  }

  // ---------- 공통: 제목 정규화 ----------
  private String norm(String s) {
    if (s == null) return "";
    String t = s.trim().toLowerCase();
    return t.replaceAll("\\s+", " ");
  }

  // ---------- Rooms ----------
  @Transactional(readOnly = true)
  public List<RoomDTO> getRooms(String contentid) {
    String cid = resolveHotelForBusiness(contentid).getContentid();

    // 최신 등록 우선(id desc)으로 받아 중복 roomtitle은 첫 개만 유지
    List<Room> all = roomRepo.findByContentidOrderByIdDesc(cid);

    Map<String, Room> picked = new LinkedHashMap<>();
    for (Room r : all) {
      String key = norm(r.getRoomtitle());
      picked.putIfAbsent(key, r); // 이미 있으면 skip → 최신 것만 남음
    }

    return picked.values().stream()
        .map(r -> new RoomDTO(
            r.getId(), r.getRoomcode(), r.getRoomtitle(), r.getRoombasecount(),
            r.getRoommaxcount(), r.getRoomoffseasonminfee1(), r.getRoompeakseasonminfee1()
        )).toList();
  }

  @Transactional
  public RoomDTO createRoom(String contentid, RoomDTO dto) {
    String cid = resolveHotelForBusiness(contentid).getContentid();

    // 대소문자/공백 무시 중복 검사
    if (roomRepo.existsNormalized(cid, dto.getRoomtitle())) {
      throw new IllegalStateException("이미 동일한 이름의 객실이 존재합니다: " + dto.getRoomtitle());
    }

    Room r = new Room();
    r.setContentid(cid);
    r.setRoomcode(dto.getRoomcode());
    r.setRoomtitle(dto.getRoomtitle());
    r.setRoombasecount(dto.getRoombasecount());
    r.setRoommaxcount(dto.getRoommaxcount());
    r.setRoomoffseasonminfee1(dto.getRoomoffseasonminfee1());
    r.setRoompeakseasonminfee1(dto.getRoompeakseasonminfee1());
    roomRepo.save(r);

    return new RoomDTO(
        r.getId(), r.getRoomcode(), r.getRoomtitle(), r.getRoombasecount(),
        r.getRoommaxcount(), r.getRoomoffseasonminfee1(), r.getRoompeakseasonminfee1()
    );
  }

  @Transactional
  public RoomDTO updateRoom(String contentid, Long roomId, RoomDTO dto) {
    // contentid로 소유 검증만 수행 (room 자체는 id로 조회)
    resolveHotelForBusiness(contentid);
    Room r = roomRepo.findById(roomId).orElseThrow();

    // 이름이 바뀌는 경우만 중복 검사 (정규화 기준)
    if (dto.getRoomtitle() != null && !norm(dto.getRoomtitle()).equals(norm(r.getRoomtitle()))) {
      if (roomRepo.existsNormalized(r.getContentid(), dto.getRoomtitle())) {
        throw new IllegalStateException("이미 동일한 이름의 객실이 존재합니다: " + dto.getRoomtitle());
      }
      r.setRoomtitle(dto.getRoomtitle());
    }

    if (dto.getRoomcode() != null) r.setRoomcode(dto.getRoomcode());
    if (dto.getRoombasecount() != null) r.setRoombasecount(dto.getRoombasecount());
    if (dto.getRoommaxcount() != null) r.setRoommaxcount(dto.getRoommaxcount());
    if (dto.getRoomoffseasonminfee1() != null) r.setRoomoffseasonminfee1(dto.getRoomoffseasonminfee1());
    if (dto.getRoompeakseasonminfee1() != null) r.setRoompeakseasonminfee1(dto.getRoompeakseasonminfee1());

    roomRepo.save(r);

    return new RoomDTO(
        r.getId(), r.getRoomcode(), r.getRoomtitle(), r.getRoombasecount(),
        r.getRoommaxcount(), r.getRoomoffseasonminfee1(), r.getRoompeakseasonminfee1()
    );
  }

  @Transactional
  public void deleteRoom(String contentid, Long roomId) {
    resolveHotelForBusiness(contentid);
    roomRepo.deleteById(roomId);
  }

  // ---------- Reservations & Payments ----------
  @Transactional(readOnly = true)
  public List<ReservationDTO> getReservations(String contentid) {
    String cid = resolveHotelForBusiness(contentid).getContentid();
    return reservationRepo.findByContentidOrderByReservationDateDesc(cid).stream()
        .map(r -> new ReservationDTO(
            r.getReservationId(),
            r.getUser() != null ? r.getUser().getUsername() : null,
            r.getUser() != null ? r.getUser().getName() : null,
            null,
            r.getUser() != null ? r.getUser().getPhoneNumber() : null,
            r.getReservName(),
            r.getReservPhone(),
            r.getCheckInDate(),
            r.getCheckOutDate(),
            r.getRoomcode(),
            r.getStatus(),
            r.getTotalPrice(),
            r.getReservationDate()
        )).toList();
  }

  @Transactional(readOnly = true)
  public List<PaymentDTO> getPaymentsForHotel(String contentid) {
    String cid = resolveHotelForBusiness(contentid).getContentid();
    List<Long> resIds = reservationRepo.findByContentidOrderByReservationDateDesc(cid).stream()
        .map(Reservation::getReservationId).toList();
    if (resIds.isEmpty()) return List.of();
    return paymentRepo.findByReservationReservationIdIn(resIds).stream()
        .map(p -> new PaymentDTO(
            p.getPaymentId(),
            p.getReservation() != null ? p.getReservation().getReservationId() : null,
            p.getUserName(),
            p.getPaymentAmount(),
            p.getPaymentMethod(),
            p.getPaymentStatus(),
            p.getPaymentDate()
        )).toList();
  }
}
