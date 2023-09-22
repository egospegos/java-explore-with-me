package ru.practicum.ewm.compilation.dto;

import lombok.Data;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.List;

@Data
public class UpdateCompilationRequest {
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
