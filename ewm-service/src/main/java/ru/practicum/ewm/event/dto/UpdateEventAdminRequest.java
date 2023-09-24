package ru.practicum.ewm.event.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.event.StateAction;
import ru.practicum.ewm.event.location.Location;
import ru.practicum.ewm.util.common.Marker;

@Data
public class UpdateEventAdminRequest {
    @Length(groups = {Marker.OnUpdate.class}, min = 20, max = 2000)
    private String annotation;

    @Length(groups = {Marker.OnUpdate.class}, min = 20, max = 7000)
    private String description;

    private String eventDate;

    private Boolean paid;

    private Long participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    @Length(groups = {Marker.OnUpdate.class}, min = 3, max = 120)
    private String title;

    private Long category;

    private Location location;
}
