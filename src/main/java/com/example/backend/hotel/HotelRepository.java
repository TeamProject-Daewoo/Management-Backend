package com.example.backend.hotel;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, String> {
    Optional<Hotel> findByContentid(String contentid);

    // 여러 호텔 소유 케이스 지원
    List<Hotel> findAllByBusinessRegistrationNumber(String businessRegistrationNumber);
}
