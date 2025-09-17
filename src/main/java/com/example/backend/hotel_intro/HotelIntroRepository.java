package com.example.backend.hotel_intro;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelIntroRepository extends JpaRepository<HotelIntro, Long> {
	Optional<HotelIntro> findTopByContentidOrderByIdDesc(String contentid);
}
