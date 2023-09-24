package ru.practicum.ewm.request;


import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto createRequestPrivate(long userId, long eventId);

    ParticipationRequestDto updateRequestPrivate(long userId, long requestId);

    List<ParticipationRequestDto> getAllRequestsPrivate(long userId);
}
