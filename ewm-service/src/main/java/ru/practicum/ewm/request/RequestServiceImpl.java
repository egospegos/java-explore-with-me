package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private final RequestMapper requestMapper = Mappers.getMapper(RequestMapper.class);

    @Override
    public ParticipationRequestDto createRequestPrivate(long userId, long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalStateException("Wrong event id=" + eventId));


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Wrong user id=" + userId));

        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setStatus(Status.PENDING);
        request.setRequester(user);
        request.setEvent(event);

        return requestMapper.requestToParticipationRequestDto(requestRepository.save(request));

    }

    @Override
    public ParticipationRequestDto updateRequestPrivate(long userId, long requestId) {
        Request request = requestRepository.findByIdAndUserId(requestId, userId);
        request.setStatus(Status.CANCELED);
        return requestMapper.requestToParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getAllRequestsPrivate(long userId) {
        List<Request> requests = requestRepository.findAllByUserId(userId);
        List<ParticipationRequestDto> dtos = new ArrayList<>();
        for (Request request : requests) {
            dtos.add(requestMapper.requestToParticipationRequestDto(request));
        }
        return dtos;
    }
}
