package ru.practicum.ewm.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

@Mapper
public interface RequestMapper {

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "created", source = "entity.created")
    @Mapping(target = "status", source = "entity.status")
    @Mapping(target = "event", expression = "java(entity.getEvent().getId())")
    @Mapping(target = "requester", expression = "java(entity.getRequester().getId())")
    ParticipationRequestDto requestToParticipationRequestDto(Request entity);
}
