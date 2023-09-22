package ru.practicum.ewm.event.dto;

import lombok.Data;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.event.StateAction;
import ru.practicum.ewm.event.location.Location;

@Data
public class UpdateEventUserRequest {

    private String annotation;

    private String description;

    private String eventDate;

    private Boolean paid;

    private Long participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    private String title;

    private Category category;

    private Location location;
}
