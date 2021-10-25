package com.quintor.worqplace.presentation.dto.room;

import com.quintor.worqplace.domain.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoomMapper {
	RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

	@Mapping(source = "room.id", target = "id")
	RoomDTO toRoomDTO(Room room);
}
