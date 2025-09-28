package com.example.backend.hotel;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.backend.admindashboard.AdminDashboardHotelsDto;

public interface HotelRepository extends JpaRepository<Hotel, String> {
    Optional<Hotel> findByContentid(String contentid);

    // 여러 호텔 소유 케이스 지원
    List<Hotel> findAllByBusinessRegistrationNumber(String businessRegistrationNumber);

    boolean existsByContentid(String contentid);

    @Query("SELECT DISTINCT new com.example.backend.admindashboard.AdminDashboardHotelsDto( " +
       "h.contentid, h.title, r.name, h.businessRegistrationNumber) " +
       "FROM Hotel h, Region r " +
       "WHERE h.areaCode = r.areaCode " +
       "AND h.sigunguCode = r.code " +
       "AND h.businessRegistrationNumber IS NOT NULL")
    List<AdminDashboardHotelsDto> findAdminDashboard();

    @Query("SELECT DISTINCT new com.example.backend.admindashboard.AdminDashboardHotelsDto( " +
       "h.contentid, h.title, r.name, h.businessRegistrationNumber) " +
       "FROM Hotel h, Region r " +
       "WHERE h.areaCode = r.areaCode " +
       "AND h.sigunguCode = r.code " +
       "AND h.businessRegistrationNumber = :businessNumber")
    List<AdminDashboardHotelsDto> findAdminDashboardByBusinessNumber(@Param("businessNumber") String businessNumber);
}
