package com.example.backend.hotel;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByContentid(String contentid);
}
