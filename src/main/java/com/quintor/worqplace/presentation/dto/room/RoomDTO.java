package com.quintor.worqplace.presentation.dto.room;

import com.quintor.worqplace.domain.Room;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomDTO {
	List<Room> rooms;
}
