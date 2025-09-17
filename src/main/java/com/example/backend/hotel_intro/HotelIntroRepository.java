package com.example.backend.hotel_intro;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HotelIntroRepository extends JpaRepository<HotelIntro, Long> {
  Optional<HotelIntro> findTopByContentidOrderByIdDesc(String contentid);
}
