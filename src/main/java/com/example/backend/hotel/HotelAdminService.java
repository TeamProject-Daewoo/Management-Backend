package com.example.backend.hotel;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

  // ====================== 내부 유틸 ======================
  /** 현재 로그인 사용자 */
  private User currentUserOrThrow() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getName() == null) {
      throw new IllegalStateException("인증 정보가 없습니다.");
    }
    return userRepo.findByUsername(auth.getName())
        .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다. username=" + auth.getName()));
  }

  /** 현재 BUSINESS 사용자 검사 + BRN 반환 */
  private String currentBusinessNumberOrThrow() {
    User u = currentUserOrThrow();
    if (u.getRole() != Role.BUSINESS) {
      throw new IllegalStateException("BUSINESS 권한 사용자만 접근 가능합니다.");
    }
    String brn = u.getBusiness_registration_number();
    if (brn == null || brn.isBlank()) {
      throw new IllegalStateException("사용자에 사업자번호가 없습니다.");
    }
    return brn;
  }

  /** 현재 BUSINESS 사용자가 소유한 모든 호텔 목록 */
  @Transactional(readOnly = true)
  public List<Hotel> getBusinessHotelsOrThrow() {
    String brn = currentBusinessNumberOrThrow();
    List<Hotel> hotels = hotelRepo.findAllByBusinessRegistrationNumber(brn);
    if (hotels.isEmpty()) {
      throw new IllegalStateException("사업자번호에 해당하는 호텔이 없습니다. brn=" + brn);
    }
    return hotels;
  }

  /** contentid 파라미터 해석: 넘겨지면 소유 검증, 없으면 목록 첫 번째 선택 */
  @Transactional(readOnly = true)
  public Hotel resolveHotelForBusiness(String contentid) {
    List<Hotel> hotels = getBusinessHotelsOrThrow();
    if (contentid == null || contentid.isBlank()) {
      return hotels.get(0); // 기본 정책: 첫 호텔 선택
    }
    return hotels.stream()
        .filter(h -> contentid.equals(h.getContentid()))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(
            "해당 contentid의 호텔이 이 사업자 소유가 아닙니다: " + contentid));
  }

  // ====================== Public APIs ======================

  // ----- Hotel Basic -----
  @Transactional(readOnly = true)
  public List<HotelDTO> listMyHotels() {
    return getBusinessHotelsOrThrow().stream()
        .map(HotelDTO::from)
        .toList();
  }

  @Transactional(readOnly = true)
  public HotelDTO getHotelBasic(String contentid) {
    Hotel h = resolveHotelForBusiness(contentid);
    return HotelDTO.from(h);
  }

  @Transactional
  public HotelDTO updateHotelBasic(String contentid, HotelUpdateRequest req) {
    Hotel h = resolveHotelForBusiness(contentid);
    if (req.getTitle() != null)
      h.setTitle(req.getTitle());
    if (req.getAddr1() != null)
      h.setAddr1(req.getAddr1());
    if (req.getTel() != null)
      h.setTel(req.getTel());
    if (req.getFirstimage() != null)
      h.setFirstimage(req.getFirstimage());
    if (req.getMapx() != null)
      h.setMapx(req.getMapx());
    if (req.getMapy() != null)
      h.setMapy(req.getMapy());
    hotelRepo.save(h);
    return getHotelBasic(contentid);
  }

  // ----- Hotel Intro -----
  @Transactional(readOnly = true)
  public HotelIntroDTO getHotelIntro(String contentid) {
    String cid = resolveHotelForBusiness(contentid).getContentid();
    HotelIntro i = introRepo.findTopByContentidOrderByIdDesc(cid).orElse(null);
    if (i == null)
      return null;
    return HotelIntroDTO.from(i);
  }

  @Transactional
  public HotelIntroDTO upsertHotelIntro(String contentid, HotelIntroUpdateRequest r) {
    String cid = resolveHotelForBusiness(contentid).getContentid();
    HotelIntro i = introRepo.findTopByContentidOrderByIdDesc(cid)
        .orElseGet(() -> {
          HotelIntro x = new HotelIntro();
          x.setContentid(cid);
          return x;
        });
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

  // ----- 공통 유틸 -----
  private String norm(String s) {
    if (s == null)
      return "";
    String t = s.trim().toLowerCase();
    return t.replaceAll("\\s+", " ");
  }

  // ----- Rooms -----
  @Transactional(readOnly = true)
  public List<RoomDTO> getRooms(String contentid) {
    String cid = resolveHotelForBusiness(contentid).getContentid();
    List<Room> all = roomRepo.findByContentidOrderByIdDesc(cid);

    Map<String, Room> picked = new LinkedHashMap<>();
    for (Room r : all) {
      String key = norm(r.getRoomtitle());
      picked.putIfAbsent(key, r); // 최신 것만 남김
    }

    return picked.values().stream()
        .map(RoomDTO::from)
        .toList();
  }

  @Transactional
  public RoomDTO createRoom(String contentid, RoomDTO dto) {
    String cid = resolveHotelForBusiness(contentid).getContentid();
    if (roomRepo.existsNormalized(cid, dto.getRoomtitle())) {
      throw new IllegalStateException("이미 동일한 이름의 객실이 존재합니다: " + dto.getRoomtitle());
    }
    Room r = dto.toEntity(cid);
    roomRepo.save(r);
    return RoomDTO.from(r);
  }

  @Transactional
  public RoomDTO updateRoom(String contentid, Long roomId, RoomDTO dto) {
    resolveHotelForBusiness(contentid);
    Room r = roomRepo.findById(roomId).orElseThrow();

    if (dto.getRoomtitle() != null &&
        !norm(dto.getRoomtitle()).equals(norm(r.getRoomtitle()))) {
      if (roomRepo.existsNormalized(r.getContentid(), dto.getRoomtitle())) {
        throw new IllegalStateException("이미 동일한 이름의 객실이 존재합니다: " + dto.getRoomtitle());
      }
      r.setRoomtitle(dto.getRoomtitle());
    }

    if (dto.getRoomcode() != null)
      r.setRoomcode(dto.getRoomcode());
    if (dto.getRoombasecount() != null)
      r.setRoombasecount(dto.getRoombasecount());
    if (dto.getRoommaxcount() != null)
      r.setRoommaxcount(dto.getRoommaxcount());
    if (dto.getRoomcount() != null)
      r.setRoomcount(dto.getRoomcount());
    if (dto.getRoomoffseasonminfee1() != null)
      r.setRoomoffseasonminfee1(dto.getRoomoffseasonminfee1());
    if (dto.getRoompeakseasonminfee1() != null)
      r.setRoompeakseasonminfee1(dto.getRoompeakseasonminfee1());

    roomRepo.save(r);
    return RoomDTO.from(r);
  }

  @Transactional
  public void deleteRoom(String contentid, Long roomId) {
    resolveHotelForBusiness(contentid);
    roomRepo.deleteById(roomId);
  }

  // ----- Reservations & Payments -----
  @Transactional(readOnly = true)
  public List<ReservationDTO> getReservations(String contentid) {
    List<Reservation> reservations;
    if (contentid == null || contentid.isBlank()) {
      reservations = getBusinessHotelsOrThrow().stream()
          .flatMap(h -> reservationRepo.findByContentidOrderByReservationDateDesc(h.getContentid()).stream())
          .toList();
    } else {
      String cid = resolveHotelForBusiness(contentid).getContentid();
      reservations = reservationRepo.findByContentidOrderByReservationDateDesc(cid);
    }

    Map<String, String> roomMap = roomRepo.findAll().stream()
        .collect(Collectors.toMap(
            r -> r.getContentid() + "::" + r.getRoomcode(),
            Room::getRoomtitle,
            (a, b) -> a));

    return reservations.stream()
        .map(r -> toDtoWithPayment(r, roomMap))
        .toList();
  }

  private ReservationDTO toDtoWithPayment(Reservation r, Map<String, String> roomMap) {
    var payments = paymentRepo.findByReservationReservationId(r.getReservationId());

    String payStatus = null;
    Long payId = null;
    LocalDateTime payDate = null;
    Integer payAmount = null;

    if (!payments.isEmpty()) {
      var latest = payments.get(payments.size() - 1);
      payStatus = latest.getPaymentStatus();
      payId = latest.getPaymentId();
      payDate = latest.getPaymentDate();
      payAmount = latest.getPaymentAmount();
    }

    String roomKey = (r.getContentid() != null && r.getRoomcode() != null)
        ? r.getContentid() + "::" + r.getRoomcode()
        : null;
    String roomTitle = roomKey != null ? roomMap.get(roomKey) : null;

    return new ReservationDTO(
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
        roomTitle,
        r.getStatus(),
        r.getTotalPrice(),
        r.getReservationDate(),
        r.getNumAdults(),
        r.getNumChildren(),
        payStatus,
        payId,
        payDate,
        payAmount);
  }

  @Transactional(readOnly = true)
  public List<PaymentDTO> getPaymentsForHotel(String contentid) {
    String cid = resolveHotelForBusiness(contentid).getContentid();
    List<Long> resIds = reservationRepo.findByContentidOrderByReservationDateDesc(cid).stream()
        .map(Reservation::getReservationId)
        .toList();
    if (resIds.isEmpty())
      return List.of();

    return paymentRepo.findByReservationReservationIdIn(resIds).stream()
        .map(p -> new PaymentDTO(
            p.getPaymentId(),
            p.getReservation() != null ? p.getReservation().getReservationId() : null,
            p.getUserName(),
            p.getPaymentAmount(),
            p.getPaymentMethod(),
            p.getPaymentStatus(),
            p.getPaymentDate()))
        .toList();
  }

  @Transactional
  public void processBulkReservations(String contentid, List<Long> ids, String action) {
    resolveHotelForBusiness(contentid);
    switch (action) {
      case "paidreservation" -> reservationRepo.updateStatus(ids, "PAID");
      case "paidpayment" -> paymentRepo.updatePaymentStatus(ids, "PAID");
      case "cancel" -> reservationRepo.updateStatus(ids, "CANCELLED");
      case "refund" -> paymentRepo.updatePaymentStatus(ids, "CANCELED");
      default -> throw new IllegalArgumentException("Unknown action: " + action);
    }
  }

  @Transactional
  public ReservationDTO updateReservationStatus(Long id, String status) {
    Reservation reservation = reservationRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("예약 없음"));
    reservation.setStatus(status);
    reservationRepo.save(reservation);
    return ReservationDTO.from(reservation);
  }

  @Transactional
  public void updatePaymentStatus(Long id, String status) {
    var payment = paymentRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("결제 없음"));
    payment.setPaymentStatus(status);
    paymentRepo.save(payment);
  }

  @Transactional
  public ReservationDTO updateReservation(Long id, ReservationDTO dto) {
    Reservation r = reservationRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다. id=" + id));

    if (dto.getCheckInDate() != null)
      r.setCheckInDate(dto.getCheckInDate());
    if (dto.getCheckOutDate() != null)
      r.setCheckOutDate(dto.getCheckOutDate());
    if (dto.getNumAdults() != null)
      r.setNumAdults(dto.getNumAdults());
    if (dto.getNumChildren() != null)
      r.setNumChildren(dto.getNumChildren());
    if (dto.getStatus() != null)
      r.setStatus(dto.getStatus());
    reservationRepo.save(r);

    if (dto.getPaymentStatus() != null && dto.getPaymentId() != null) {
      paymentRepo.findById(dto.getPaymentId()).ifPresent(p -> {
        p.setPaymentStatus(dto.getPaymentStatus());
        paymentRepo.save(p);
      });
    }
    return ReservationDTO.from(r);
  }

  // ----- Hotel Register -----
  private String generateContentId() {
    String id;
    do {
      id = String.valueOf((int) (Math.random() * 9000000) + 1000000); // 7자리 난수
    } while (hotelRepo.existsByContentid(id));
    return id;
  }

  @Transactional
  public HotelDTO registerHotel(HotelRegisterRequest req) {
    String cid = generateContentId();

    // 호텔
    Hotel h = req.getHotel().toEntity();
    h.setContentid(cid);
    h.setBusinessRegistrationNumber(currentBusinessNumberOrThrow());
    hotelRepo.save(h);

    // 인트로
    HotelIntro i = req.getIntro().toEntity(cid);
    introRepo.save(i);

    // 객실
    for (RoomDTO dto : req.getRooms()) {
      roomRepo.save(dto.toEntity(cid));
    }

    return HotelDTO.from(h);
  }
}
