package ru.practicum.ewm.request.dto;

import lombok.Data;
import ru.practicum.ewm.request.Status;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private Status status;
}
