package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.location.LocationRepository;
import ru.practicum.ewm.exception.DublicateException;
import ru.practicum.ewm.user.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    private final EventMapper eventMapper = Mappers.getMapper(EventMapper.class);


    @Override
    public EventFullDto createEventPrivate(NewEventDto newEventDto, long userId) {
        Event event = eventMapper.newEventDtoToEvent(newEventDto);
        event.setCategory(categoryRepository.findById(newEventDto.getCategoryId())
                .orElseThrow(() -> new IllegalStateException("Wrong category id=" + newEventDto.getCategoryId())));
        event.setCreatedOn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));


        event.setState(State.PENDING);
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Wrong user id=" + userId)));

        return eventMapper.eventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getAllEventsPrivate(long userId, Long from, Long size) {
        List<Event> events = new ArrayList<>();
        long start = from / size;
        events = eventRepository.findAllEventsByUserIdPrivate(userId, PageRequest.of(Math.toIntExact(start), Math.toIntExact(size),
                Sort.by("event_date").ascending())).getContent();

        List<EventShortDto> eventsDto = new ArrayList<>();
        for (Event event : events) {
            EventShortDto eventShortDto = eventMapper.eventToEventShortDto(event);
            eventsDto.add(eventShortDto);

        }
        return eventsDto;
    }

    @Override
    public EventFullDto getEventByIdPrivate(long eventId, long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Wrong user id=" + userId));
        eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalStateException("Wrong event id=" + eventId));


        Event event = eventRepository.findByIdAndUserId(eventId, userId);
        if (event == null) {
            throw new IllegalStateException("No such event with id=" + eventId + " and userId =" + userId);
        }
        return eventMapper.eventToEventFullDto(event);
    }

    @Override
    public EventFullDto updateEventPrivate(UpdateEventUserRequest updateEvent, long eventId, long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Wrong user id=" + userId));
        eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalStateException("Wrong event id=" + eventId));

        Event event = eventRepository.findByIdAndUserId(eventId, userId);
        if (event.getState() == State.PUBLISHED) {
            throw new DublicateException("Event with id=" + eventId + " is PUBLISHED");
        }
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            event.setCategory(updateEvent.getCategory());
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(updateEvent.getLocation());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        if (updateEvent.getStateAction() == StateAction.SEND_TO_REVIEW) {
            event.setState(State.PUBLISHED);
        }
        if (updateEvent.getStateAction() == StateAction.CANCEL_REVIEW) {
            event.setState(State.CANCELED);
        }

        //вносим измененный event
        return eventMapper.eventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getAllEventsAdmin(List<Integer> users, List<String> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Long from, Long size) {
        List<Event> events = new ArrayList<>();
        long start = from / size;

        events = eventRepository.findAllEventsAdmin(users, states, categories, rangeStart, rangeEnd, PageRequest.of(Math.toIntExact(start), Math.toIntExact(size),
                Sort.by("id").ascending())).getContent();

        List<EventFullDto> eventsDto = new ArrayList<>();
        for (Event event : events) {
            EventFullDto eventFullDto = eventMapper.eventToEventFullDto(event);
            eventsDto.add(eventFullDto);
        }
        return eventsDto;
    }

    @Override
    public EventFullDto updateEventAdmin(UpdateEventAdminRequest updateEvent, long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalStateException("Wrong event id=" + eventId));

        if (event.getState() == State.PUBLISHED) {
            throw new DublicateException("Event with id=" + eventId + " is PUBLISHED");
        }
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            event.setCategory(updateEvent.getCategory());
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(updateEvent.getLocation());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        if (updateEvent.getStateAction() == StateAction.PUBLISH_EVENT && event.getState() == State.PENDING) {
            event.setState(State.PUBLISHED);
        }
        if (updateEvent.getStateAction() == StateAction.REJECT_EVENT && event.getState() == State.PENDING) {
            event.setState(State.CANCELED);
        }

        //вносим измененный event
        return eventMapper.eventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEventByIdPublic(long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Wrong event id=" + id));

        return eventMapper.eventToEventFullDto(event);
    }
}
