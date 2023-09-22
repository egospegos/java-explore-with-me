package ru.practicum.ewm.compilation.dto;

import lombok.Data;
import ru.practicum.ewm.util.common.Marker;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotBlank(groups = {Marker.OnCreate.class})
    private String title;
}
