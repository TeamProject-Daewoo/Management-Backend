package com.example.backend.room;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
	List<Room> findByContentid(String contentid);
}
