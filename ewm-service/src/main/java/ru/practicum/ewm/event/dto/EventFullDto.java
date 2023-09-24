package ru.practicum.ewm.event.dto;

import lombok.Data;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.event.State;
import ru.practicum.ewm.event.location.Location;
import ru.practicum.ewm.user.User;

@Data
public class EventFullDto {

    private Long id;

    private String annotation;

    private long confirmedRequests;

    private String createdOn;

    private String description;

    private String eventDate;

    private boolean paid;

    private long participantLimit;

    private String publishedOn;

    private boolean requestModeration;

    private State state;

    private String title;

    private long views;

    private Category category;

    private User initiator;

    private Location location;
}
