package ru.practicum.ewm.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.IViewStats;
import ru.practicum.ewm.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    EndpointHitDto addHit(EndpointHitDto endpointHitDto);

    List<IViewStats> viewStats(LocalDateTime start, LocalDateTime end, String uri, boolean unique);
}
