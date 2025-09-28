package com.example.backend.admindashboard;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminDashboardController {

    private final AdminDashboardService adminService ;

    @GetMapping("/hotels")
    public ResponseEntity<List<AdminDashboardResponseDto>> getAllHotel() {
        return ResponseEntity.ok(adminService.getAllHotel(null));
    }

    @GetMapping("/bushotels")
    public ResponseEntity<List<AdminDashboardResponseDto>> getHotelByBusinessNumber(@RequestParam String businessNumber) {
        return ResponseEntity.ok(adminService.getAllHotel(businessNumber));
    }
}
