package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.IViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    EndpointHitDto addHit(EndpointHitDto endpointHitDto);

    List<IViewStats> viewStats(LocalDateTime start, LocalDateTime end, List<String> uri, boolean unique);
}
