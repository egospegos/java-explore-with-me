package ru.practicum.ewm.compilation.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.util.common.Marker;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotBlank(groups = {Marker.OnCreate.class})
    @Length(groups = {Marker.OnCreate.class}, min = 1, max = 50)
    private String title;
}
