package ru.practicum.ewm.event;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto createEventPrivate(NewEventDto newEventDto, long userId);

    List<EventShortDto> getAllEventsPrivate(long userId, Long from, Long size);

    EventFullDto getEventByIdPrivate(long eventId, long userId);

    EventFullDto updateEventPrivate(UpdateEventUserRequest updateEvent, long eventId, long userId);

    List<ParticipationRequestDto> getAllEventRequestsPrivate(long userId, long eventId);

    EventRequestStatusUpdateResult updateEventRequestsPrivate(long userId, long eventId,
                                                              EventRequestStatusUpdateRequest updateRequestStatus);

    List<EventFullDto> getAllEventsAdmin(List<Integer> users, List<String> states, List<Integer> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Long from, Long size);

    EventFullDto updateEventAdmin(UpdateEventAdminRequest updateEvent, long eventId);


    List<EventShortDto> getAllEventsPublic(String text, List<Integer> categories, Boolean paid,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                           String sort, Long from, Long size, HttpServletRequest httpServletRequest);

    EventFullDto getEventByIdPublic(long id, HttpServletRequest httpServletRequest);


}
