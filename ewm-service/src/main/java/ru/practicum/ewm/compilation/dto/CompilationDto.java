package ru.practicum.ewm.compilation.dto;

import lombok.Data;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompilationDto {
    private Long id;

    private List<EventShortDto> events = new ArrayList<>();
    private Boolean pinned;
    private String title;
}
