package com.quintor.worqplace.data;

import com.quintor.worqplace.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

}