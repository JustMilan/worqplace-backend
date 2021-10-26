package com.quintor.worqplace.presentation.dto.room;

import com.quintor.worqplace.domain.Room;
import com.quintor.worqplace.domain.Workplace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
	RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

	@Mapping(source = "room.workplaces", target = "capacity", qualifiedByName = "workplaceAmountToSize")
	RoomDTO toRoomDTO(Room room);

	@Named("workplaceAmountToSize")
	static int workplaceAmountToSize(List<Workplace> workplaces) {
		return workplaces.size();
	}
}
