package ru.practicum.ewm.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EndpointHitMapper {

    @Mapping(target = "app", source = "entity.app")
    @Mapping(target = "uri", source = "entity.uri")
    @Mapping(target = "ip", source = "entity.ip")
    EndpointHit dtoToEndpointHit(EndpointHitDto entity);

    @Mapping(target = "app", source = "entity.app")
    @Mapping(target = "uri", source = "entity.uri")
    @Mapping(target = "ip", source = "entity.ip")
    EndpointHitDto endpointHitToDto(EndpointHit entity);
}
