package com.quintor.worqplace.presentation.dto.workplace;

import com.quintor.worqplace.domain.Workplace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WorkplaceMapper {
    WorkplaceMapper INSTANCE = Mappers.getMapper(WorkplaceMapper.class);

    @Mapping(source = "workplace.room.floor", target = "floor")
    WorkplaceDTO toWorkplaceDTO(Workplace workplace);
}
