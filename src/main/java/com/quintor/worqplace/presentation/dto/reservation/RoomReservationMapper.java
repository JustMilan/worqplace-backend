package com.quintor.worqplace.presentation.dto.reservation;

import com.quintor.worqplace.domain.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomReservationMapper {
	RoomReservationMapper INSTANCE = Mappers.getMapper(RoomReservationMapper.class);

	@Mapping(source = "employee.id", target = "employeeId")
	@Mapping(source = "room.id", target = "roomId")
	List<RoomReservationDTO> toRoomReservationDTO(List<Reservation> reservation);
}
