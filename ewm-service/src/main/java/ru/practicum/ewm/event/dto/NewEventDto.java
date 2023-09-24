package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.event.location.Location;
import ru.practicum.ewm.util.common.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NewEventDto {

    private Long id;
    @NotBlank(groups = {Marker.OnCreate.class})
    @Length(groups = {Marker.OnCreate.class}, min = 20, max = 2000)
    private String annotation;
    @NotNull(groups = {Marker.OnCreate.class})
    private Long category;
    @NotBlank(groups = {Marker.OnCreate.class})
    @Length(groups = {Marker.OnCreate.class}, min = 20, max = 7000)
    private String description;
    @NotBlank(groups = {Marker.OnCreate.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;
    @NotNull(groups = {Marker.OnCreate.class})
    private Location location;

    private boolean paid;
    private long participantLimit;
    private boolean requestModeration = true;

    @NotBlank(groups = {Marker.OnCreate.class})
    @Length(groups = {Marker.OnCreate.class}, min = 3, max = 120)
    private String title;

}
