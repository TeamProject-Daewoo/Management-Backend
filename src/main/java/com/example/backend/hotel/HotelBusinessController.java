package com.example.backend.hotel;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.hotel_intro.HotelIntroDTO;
import com.example.backend.hotel_intro.HotelIntroUpdateRequest;
import com.example.backend.payment.PaymentDTO;
import com.example.backend.reservation.BulkRequest;
import com.example.backend.reservation.ReservationDTO;
import com.example.backend.room.RoomDTO;
import com.example.backend.setPrice.DeleteSpecialPriceRequestDto;
import com.example.backend.setPrice.PriceOverrideRequestDTO;
import com.example.backend.setPrice.PriceOverrideService;
import com.example.backend.setPrice.SpecialPriceGroupDto;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/business")
public class HotelBusinessController {

    private final HotelBusinessService svc;
    private final S3Presigner presigner;
    private final PriceOverrideService priceOverrideService;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.region}")
    private String region;

    // ====== 호텔 ======
    @GetMapping("/hotels")
    public List<HotelDTO> listMyHotels() {
        return svc.listMyHotels();
    }

    @GetMapping("/hotel")
    public HotelDTO getHotel(@RequestParam(value = "contentid", required = false) String contentid) {
        return svc.getHotelBasic(contentid);
    }

    @PutMapping("/hotel")
    public HotelDTO updateHotel(@RequestParam(value = "contentid", required = false) String contentid,
            @RequestBody HotelUpdateRequest req) {
        return svc.updateHotelBasic(contentid, req);
    }

    @PostMapping("/hotel/register")
    public HotelDTO registerHotel(@RequestBody HotelRegisterRequest req) {
        return svc.registerHotel(req);
    }

    // ====== 인트로 ======
    @GetMapping("/hotel/intro")
    public HotelIntroDTO getIntro(@RequestParam(value = "contentid", required = false) String contentid) {
        return svc.getHotelIntro(contentid);
    }

    @PutMapping("/hotel/intro")
    public HotelIntroDTO upsertIntro(@RequestParam(value = "contentid", required = false) String contentid,
            @RequestBody HotelIntroUpdateRequest req) {
        return svc.upsertHotelIntro(contentid, req);
    }

    // ====== 객실 ======
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
            @PathVariable Long id,
            @RequestBody RoomDTO dto) {
        return svc.updateRoom(contentid, id, dto);
    }

    @DeleteMapping("/rooms/{id}")
    public void deleteRoom(@RequestParam(value = "contentid", required = false) String contentid,
            @PathVariable Long id) {
        svc.deleteRoom(contentid, id);
    }

    // ====== 예약 / 결제 ======
    @GetMapping("/reservations")
    public List<ReservationDTO> reservations(@RequestParam(value = "contentid", required = false) String contentid) {
        return svc.getReservations(contentid);
    }

    @PutMapping("/reservations/{id}/status")
    public ReservationDTO updateReservationStatus(@PathVariable Long id,
            @RequestParam String status) {
        return svc.updateReservationStatus(id, status);
    }

    @PutMapping("/reservations/{id}")
    public ReservationDTO updateReservation(@PathVariable Long id,
            @RequestBody ReservationDTO dto) {
        return svc.updateReservation(id, dto);
    }

    @PostMapping("/reservations/bulk")
    public ResponseEntity<Void> bulkReservations(@RequestParam(required = false) String contentid,
            @RequestBody BulkRequest req) {
        svc.processBulkReservations(contentid, req.getIds(), req.getAction());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/payments")
    public List<PaymentDTO> payments(@RequestParam(value = "contentid", required = false) String contentid) {
        return svc.getPaymentsForHotel(contentid);
    }

    @PutMapping("/payments/{id}/status")
    public ResponseEntity<Void> updatePaymentStatus(@PathVariable Long id,
            @RequestParam String status) {
        svc.updatePaymentStatus(id, status);
        return ResponseEntity.ok().build();
    }

    // ====== S3 Presigned URL ======
    @GetMapping("/s3/presign")
    public Map<String, String> getPresignedUrl(@RequestParam String filename,
            @RequestParam String contentType) {
        
        String key = "hotels/" + UUID.randomUUID() + "-" + filename;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);

        return Map.of(
                "url", presignedRequest.url().toString(),
                "publicUrl", String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, key));
    }

    @PostMapping("checkVaild")    
    public ResponseEntity<Boolean> isValidFile(@RequestParam MultipartFile fileObject) throws Exception {
        System.out.println("컨트롤러 진입");
        Boolean b = svc.isImageFile(fileObject);
        System.out.println(b);
        return ResponseEntity.ok(b);
    }

    // ====== 특별가(가격) 관리 ======
    @PostMapping("/prices/override")
    public ResponseEntity<String> createPriceOverrides(@RequestBody PriceOverrideRequestDTO requestDTO) {
        priceOverrideService.createOrUpdatePriceOverrides(requestDTO);
        return ResponseEntity.ok("특별가 설정이 성공적으로 저장되었습니다.");
    }

    @GetMapping("/prices/list")
    public ResponseEntity<List<SpecialPriceGroupDto>> getSpecialPriceOverrides(
            @RequestParam(value = "contentid", required = false) String contentid) {
        List<SpecialPriceGroupDto> specialPrices = priceOverrideService.findSpecialPricesByContentId(contentid);
        return ResponseEntity.ok(specialPrices);
    }

    @DeleteMapping("/prices/delete")
    public ResponseEntity<Void> deleteSpecialPriceOverride(@RequestBody DeleteSpecialPriceRequestDto requestDto) {
        priceOverrideService.deleteSpecialPriceGroup(
                requestDto.getTitle(),
                requestDto.getStartDate(),
                requestDto.getEndDate(),
                requestDto.getHotelContentId());
        return ResponseEntity.ok().build();
    }
}
