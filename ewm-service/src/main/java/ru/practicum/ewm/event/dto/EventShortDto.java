package ru.practicum.ewm.event.dto;

import lombok.Data;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.user.User;

@Data
public class EventShortDto {
    private Long id;

    private String annotation;

    private long confirmedRequests;

    private String eventDate;

    private boolean paid;

    private String title;

    private long views;

    private Category category;

    private User initiator;


}
