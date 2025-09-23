//package com.example.backend.setPrice;
//
//import java.util.List;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequestMapping("/business")
//@RequiredArgsConstructor
//public class SetPriceController {
//	
//	private final PriceOverrideService priceOverrideService;
//	
//	@PostMapping("/prices/override")
//    @PreAuthorize("hasRole('BUSINESS')")
//    public ResponseEntity<String> createPriceOverrides(@RequestBody PriceOverrideRequestDTO requestDTO) {
//        priceOverrideService.createOrUpdatePriceOverrides(requestDTO);
//        return ResponseEntity.ok("특별가 설정이 성공적으로 저장되었습니다.");
//    }
//	
//    @GetMapping("/overrides/{contentid}")
//    // [수정] Authentication 파라미터 삭제
//    public ResponseEntity<List<SpecialPriceGroupDto>> getSpecialPriceOverrides(
//            @PathVariable String contentid
//    ) {
//        // [수정] contentid만 서비스로 전달
//        List<SpecialPriceGroupDto> specialPrices = priceOverrideService.findSpecialPricesByContentId(contentid);
//        
//        return ResponseEntity.ok(specialPrices);
//    }
//}
