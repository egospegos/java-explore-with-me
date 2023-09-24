package ru.practicum.ewm.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;

@Mapper
public interface EventMapper {

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "annotation", source = "entity.annotation")
    @Mapping(target = "description", source = "entity.description")
    @Mapping(target = "eventDate", source = "entity.eventDate")
    @Mapping(target = "location", source = "entity.location")
    @Mapping(target = "paid", source = "entity.paid")
    @Mapping(target = "participantLimit", source = "entity.participantLimit")
    @Mapping(target = "requestModeration", source = "entity.requestModeration")
    @Mapping(target = "title", source = "entity.title")
    Event newEventDtoToEvent(NewEventDto entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "annotation", source = "entity.annotation")
    @Mapping(target = "confirmedRequests", source = "entity.confirmedRequests")
    @Mapping(target = "createdOn", source = "entity.createdOn")
    @Mapping(target = "description", source = "entity.description")
    @Mapping(target = "eventDate", source = "entity.eventDate")
    @Mapping(target = "paid", source = "entity.paid")
    @Mapping(target = "participantLimit", source = "entity.participantLimit")
    @Mapping(target = "publishedOn", source = "entity.publishedOn")
    @Mapping(target = "requestModeration", source = "entity.requestModeration")
    @Mapping(target = "state", source = "entity.state")
    @Mapping(target = "title", source = "entity.title")
    @Mapping(target = "views", source = "entity.views")
    @Mapping(target = "category", source = "entity.categoryEntity")
    @Mapping(target = "initiator", source = "entity.initiator")
    @Mapping(target = "location", source = "entity.location")
    EventFullDto eventToEventFullDto(Event entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "annotation", source = "entity.annotation")
    @Mapping(target = "confirmedRequests", source = "entity.confirmedRequests")
    @Mapping(target = "eventDate", source = "entity.eventDate")
    @Mapping(target = "paid", source = "entity.paid")
    @Mapping(target = "title", source = "entity.title")
    @Mapping(target = "views", source = "entity.views")
    @Mapping(target = "category", source = "entity.categoryEntity")
    @Mapping(target = "initiator", source = "entity.initiator")
    EventShortDto eventToEventShortDto(Event entity);


}
