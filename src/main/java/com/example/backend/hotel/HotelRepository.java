package com.example.backend.hotel;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, String> {
  // PK가 contentid라서 기본 findById(String) 사용 가능하지만, 아래도 가능
  Optional<Hotel> findByContentid(String contentid);
}
