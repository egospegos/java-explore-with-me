package ru.practicum.ewm.event;

import ru.practicum.ewm.event.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto createEventPrivate(NewEventDto newEventDto, long userId);

    List<EventShortDto> getAllEventsPrivate(long userId, Long from, Long size);

    EventFullDto getEventByIdPrivate(long eventId, long userId);

    EventFullDto updateEventPrivate(UpdateEventUserRequest updateEvent, long eventId, long userId);

    List<EventFullDto> getAllEventsAdmin(List<Integer> users, List<String> states, List<Integer> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Long from, Long size);

    EventFullDto updateEventAdmin(UpdateEventAdminRequest updateEvent, long eventId);

    EventFullDto getEventByIdPublic(long id);
}
