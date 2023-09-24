package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.event.comment.Comment;
import ru.practicum.ewm.event.comment.CommentDto;
import ru.practicum.ewm.event.comment.CommentMapper;
import ru.practicum.ewm.event.comment.CommentRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.location.LocationRepository;
import ru.practicum.ewm.exception.DublicateException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.request.Request;
import ru.practicum.ewm.request.RequestMapper;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.Status;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;
    private final StatsClient statsClient;

    private final EventMapper eventMapper = Mappers.getMapper(EventMapper.class);
    private final RequestMapper requestMapper = Mappers.getMapper(RequestMapper.class);
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);


    @Override
    public EventFullDto createEventPrivate(NewEventDto newEventDto, long userId) {
        Event event = eventMapper.newEventDtoToEvent(newEventDto);
        event.setCategoryEntity(categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new IllegalStateException("Wrong category id=" + newEventDto.getCategory())));

        //валидация даты события
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime formatEventDate = LocalDateTime.parse(event.getEventDate(), formatter);
        if (formatEventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Wrong new eventDate =" + formatEventDate);
        }

        event.setCreatedOn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));


        event.setState(State.PENDING);
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Wrong user id=" + userId)));

        locationRepository.save(event.getLocation());
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


        if (updateEvent.getEventDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime formatEventDate = LocalDateTime.parse(updateEvent.getEventDate(), formatter);
            if (formatEventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ValidationException("Wrong new eventDate =" + formatEventDate);
            }
        }


        Event event = eventRepository.findByIdAndUserId(eventId, userId);
        if (event.getState() == State.PUBLISHED) {
            throw new DublicateException("Event with id=" + eventId + " is PUBLISHED");
        }

        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            event.setCategoryEntity(categoryRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new IllegalStateException("Wrong category id=" + updateEvent.getCategory())));
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
            event.setState(State.PENDING);
        }
        if (updateEvent.getStateAction() == StateAction.CANCEL_REVIEW) {
            event.setState(State.CANCELED);
        }

        //вносим измененный event
        return eventMapper.eventToEventFullDto(eventRepository.save(event));
    }


    @Override
    public List<ParticipationRequestDto> getAllEventRequestsPrivate(long userId, long eventId) {
        List<Request> requests = requestRepository.findAllByEventId(eventId);
        List<ParticipationRequestDto> dtos = new ArrayList<>();
        for (Request request : requests) {
            ParticipationRequestDto requestDto = requestMapper.requestToParticipationRequestDto(request);
            dtos.add(requestDto);
        }
        return dtos;
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequestsPrivate(long userId, long eventId, EventRequestStatusUpdateRequest updateRequestStatus) {
        List<Long> requestIds = updateRequestStatus.getRequestIds();
        Status status = updateRequestStatus.getStatus();

        Event event = eventRepository.findById(eventId).
                orElseThrow(() -> new IllegalStateException("Wrong event id=" + eventId));
        Long checkLimit = requestRepository.countConfirmedRequestsByEventId(eventId);
        if (new Long(event.getParticipantLimit()).equals(checkLimit)) {
            throw new DublicateException("Лимит принятых заявок у события id=" + event.getId() + " уже достигнут");
        }

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        for (Long id : requestIds) {
            Request request = requestRepository.findById(id)
                    .orElseThrow(() -> new IllegalStateException("Wrong request id=" + id));
            request.setStatus(status);
            requestRepository.save(request); //отправить обновленный запрос
            if (status == Status.CONFIRMED) {
                result.getConfirmedRequests().add(requestMapper.requestToParticipationRequestDto(request));
            }
            if (status == Status.REJECTED) {
                result.getRejectedRequests().add(requestMapper.requestToParticipationRequestDto(request));
            }

        }
        return result;
    }

    @Override
    public List<EventFullDto> getAllEventsAdmin(List<Integer> users, List<String> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Long from, Long size) {
        List<Event> events = new ArrayList<>();
        long start = from / size;

        if (users != null && states != null && categories != null && rangeStart != null && rangeEnd != null) {
            events = eventRepository.findAllEventsAdmin(users, states, categories, rangeStart, rangeEnd, PageRequest.of(Math.toIntExact(start), Math.toIntExact(size),
                    Sort.by("id").ascending())).getContent();
        }
        if (users == null && states == null && categories == null && rangeStart == null && rangeEnd == null) {
            events = eventRepository.findAllEventsAdminWithoutParams(PageRequest.of(Math.toIntExact(start), Math.toIntExact(size),
                    Sort.by("id").ascending())).getContent();
        }

        List<EventFullDto> eventsDto = new ArrayList<>();
        for (Event event : events) {
            EventFullDto eventFullDto = eventMapper.eventToEventFullDto(event);

            Long confirmedRequests = requestRepository.countConfirmedRequestsByEventId(event.getId());
            if (confirmedRequests != null) {
                eventFullDto.setConfirmedRequests(confirmedRequests);
            }

            eventsDto.add(eventFullDto);
        }
        return eventsDto;
    }

    @Override
    public EventFullDto updateEventAdmin(UpdateEventAdminRequest updateEvent, long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalStateException("Wrong event id=" + eventId));

        if (updateEvent.getEventDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime formatEventDate = LocalDateTime.parse(updateEvent.getEventDate(), formatter);
            if (formatEventDate.isBefore(LocalDateTime.now())) {
                throw new ValidationException("Wrong new eventDate =" + formatEventDate);
            }
        }


        if (event.getState() == State.PUBLISHED) {
            throw new DublicateException("Event with id=" + eventId + " is PUBLISHED");
        }
        if (event.getState() == State.CANCELED) {
            throw new DublicateException("Event with id=" + eventId + " is CANCELED");
        }
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoryRepository.findById(updateEvent.getCategory()).
                    orElseThrow(() -> new IllegalStateException("Wrong catgory id=" + updateEvent.getCategory()));

            event.setCategoryEntity(category);
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getLocation() != null) {
            updateEvent.getLocation().setId(event.getLocation().getId());
            event.setLocation(updateEvent.getLocation());
            locationRepository.save(event.getLocation());
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
    public List<EventShortDto> getAllEventsPublic(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Long from, Long size, HttpServletRequest httpServletRequest) {

        List<Event> events = new ArrayList<>();
        long start = from / size;

        if (categories != null) {
            if (categories.contains(0)) {
                throw new ValidationException("Incorrect category id");
            }
        }

        //поиск только по категориям
        if (text == null && categories != null && paid == null && rangeStart == null && rangeEnd == null) {
            events = eventRepository.findAllEventsPublicByCategories(categories, PageRequest.of(Math.toIntExact(start), Math.toIntExact(size),
                    Sort.by("event_date").ascending())).getContent();

        }

        //если ни один параметр не задан
        if (text == null && categories == null && paid == null && rangeStart == null && rangeEnd == null) {
            events = eventRepository.findAllWithPagination(PageRequest.of(Math.toIntExact(start), Math.toIntExact(size),
                    Sort.by("event_date").ascending())).getContent();

        }

        if (text != null && paid != null && categories != null && (rangeStart == null || rangeEnd == null)) {
            events = eventRepository.findAllEventsPublicWithoutRangeDate(text, categories, paid, PageRequest.of(Math.toIntExact(start), Math.toIntExact(size),
                    Sort.by("event_date").ascending())).getContent();
        }
        if (text != null && paid != null && categories != null && rangeStart != null && rangeEnd != null) {
            events = eventRepository.findAllEventsPublicWithRangeDate(text, categories, paid, rangeStart, rangeEnd, PageRequest.of(Math.toIntExact(start), Math.toIntExact(size),
                    Sort.by("event_date").ascending())).getContent();
        }


        List<EventShortDto> eventsDto = new ArrayList<>();
        for (Event event : events) {
            EventShortDto eventShortDto = eventMapper.eventToEventShortDto(event);
            eventsDto.add(eventShortDto);
        }

        //отправить hit в статистику
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp("ewm-service");
        endpointHitDto.setUri(httpServletRequest.getRequestURI());
        endpointHitDto.setTimestamp(LocalDateTime.now());
        endpointHitDto.setIp(httpServletRequest.getRemoteAddr());
        statsClient.hit(endpointHitDto);
        return eventsDto;
    }

    @Override
    public EventFullDto getEventByIdPublic(long id, HttpServletRequest httpServletRequest) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Wrong event id=" + id));

        if (event.getState() != State.PUBLISHED) {
            throw new IllegalStateException("Событие не опубликовано");
        }

        //получить количество просмотров из статистики
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = LocalDateTime.now().minusYears(100).format(formatter);
        String end = LocalDateTime.now().format(formatter);

        List<LinkedHashMap> viewStats = (List<LinkedHashMap>) statsClient.viewStats(start, end, httpServletRequest.getRequestURI(), true).getBody();

        if (viewStats.size() > 0) {
            event.setViews((Integer) viewStats.get(0).get("hits"));
        }

        //отправить hit в статистику
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp("ewm-service");
        endpointHitDto.setUri(httpServletRequest.getRequestURI());
        endpointHitDto.setTimestamp(LocalDateTime.now());
        endpointHitDto.setIp(httpServletRequest.getRemoteAddr());
        statsClient.hit(endpointHitDto);

        EventFullDto eventFullDto = eventMapper.eventToEventFullDto(event);

        //проверка есть ли комментарии у события
        List<Comment> comments = commentRepository.findAllByEventId(id);
        if (comments.size() >= 1) {
            for (Comment comment : comments) {
                eventFullDto.getComments().add(commentMapper.commentToCommentDto(comment));
            }
        }
        return eventFullDto;
    }

    @Override
    public CommentDto createCommentToEvent(CommentDto commentDto, long userId, long eventId) {
        //есть ли такое событие, чтоб коммент оставить
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalStateException("Wrong event id=" + eventId));
        //есть ли такой пользователь
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Wrong user id=" + userId));
        //был ли такой пользователь на мероприятии
        Request request = requestRepository.findByEventIdAndUserId(eventId, userId);
        if (request == null) {
            throw new IllegalStateException("Пользователь id=" + userId + " не был на мероприятии id=" + eventId);
        }


        //маппинг
        Comment comment = commentMapper.commentDtoToComment(commentDto);
        comment.setEvent(event);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        //маппинг перед отправкой назад
        return commentMapper.commentToCommentDto(commentRepository.save(comment));
    }
}
