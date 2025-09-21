package com.example.backend.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByContentid(String contentid);

    // 최신 등록순으로 받아서 서비스에서 "이름 기준" 중복 제거에 사용
    List<Room> findByContentidOrderByIdDesc(String contentid);

    Optional<Room> findTopByContentidAndRoomcodeOrderByIdDesc(String contentid, String roomcode);
    
    // 대소문자/앞뒤 공백 무시하여 중복 여부 확인
    @Query("""
        select (count(r) > 0)
        from Room r
        where r.contentid = :contentid
          and lower(trim(r.roomtitle)) = lower(trim(:roomtitle))
    """)
    boolean existsNormalized(@Param("contentid") String contentid,
                             @Param("roomtitle") String roomtitle);
}
