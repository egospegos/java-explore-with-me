package ru.practicum.ewm.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.util.common.Marker;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    //PRIVATE-------------------------------------------------------------

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getAllEventsPrivate(@PathVariable long userId,
                                                   @RequestParam(defaultValue = "0") Long from,
                                                   @RequestParam(defaultValue = "10") Long size) {
        return eventService.getAllEventsPrivate(userId, from, size);
    }

    @PostMapping("/users/{userId}/events")
    @Validated({Marker.OnCreate.class})
    public ResponseEntity<EventFullDto> createEventPrivate(@PathVariable long userId,
                                                           @RequestBody @Valid NewEventDto newEventDto) {
        return new ResponseEntity<>(eventService.createEventPrivate(newEventDto, userId), HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventByIdPrivate(@PathVariable long userId, @PathVariable long eventId) {
        return eventService.getEventByIdPrivate(eventId, userId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    @Validated({Marker.OnUpdate.class})
    public EventFullDto updateEventPrivate(@PathVariable long userId, @PathVariable long eventId,
                                           @RequestBody @Valid UpdateEventUserRequest updateEvent) {
        return eventService.updateEventPrivate(updateEvent, eventId, userId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getAllEventRequestsPrivate(@PathVariable long userId, @PathVariable long eventId) {
        return eventService.getAllEventRequestsPrivate(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequestsPrivate(@PathVariable long userId, @PathVariable long eventId,
                                                                     @RequestBody EventRequestStatusUpdateRequest updateRequestStatus) {
        return eventService.updateEventRequestsPrivate(userId, eventId, updateRequestStatus);
    }


    //ADMIN-----------------------------------------------------------

    @GetMapping("/admin/events")
    public List<EventFullDto> getAllEventsAdmin(@RequestParam(required = false) List<Integer> users,
                                                @RequestParam(required = false) List<String> states,
                                                @RequestParam(required = false) List<Integer> categories,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "0") Long from,
                                                @RequestParam(defaultValue = "10") Long size) {
        return eventService.getAllEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    @Validated({Marker.OnUpdate.class})
    public EventFullDto updateEventAdmin(@PathVariable long eventId, @RequestBody @Valid UpdateEventAdminRequest updateEvent) {
        return eventService.updateEventAdmin(updateEvent, eventId);
    }


    //PUBLIC---------------------------------------------------------

    @GetMapping("/events")
    public List<EventShortDto> getAllEventsPublic(@RequestParam(required = false) String text,
                                                  @RequestParam(required = false) List<Integer> categories,
                                                  @RequestParam(required = false) Boolean paid,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                  @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                  @RequestParam(defaultValue = "") String sort,
                                                  @RequestParam(defaultValue = "0") Long from,
                                                  @RequestParam(defaultValue = "10") Long size,
                                                  HttpServletRequest httpServletRequest) {

        return eventService.getAllEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, httpServletRequest);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventByIdPublic(@PathVariable long id, HttpServletRequest httpServletRequest) {
        return eventService.getEventByIdPublic(id, httpServletRequest);
    }


}
