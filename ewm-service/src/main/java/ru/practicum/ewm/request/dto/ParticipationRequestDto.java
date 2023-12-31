package ru.practicum.ewm.request.dto;

import lombok.Data;
import ru.practicum.ewm.request.Status;

import java.time.LocalDateTime;

@Data
public class ParticipationRequestDto {

    private Long id;

    private LocalDateTime created;

    private Status status;

    private Long event;

    private Long requester;
}
