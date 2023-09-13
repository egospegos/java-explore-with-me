package ru.practicum.ewm.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.IViewStats;
import ru.practicum.ewm.dto.ViewStats;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class StatsController {
    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }


    @PostMapping("/hit")
    public EndpointHitDto hit(@RequestBody EndpointHitDto endpointHitDto) {
        return statsService.addHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<IViewStats> viewStats(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                      @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                      @RequestParam(name = "uris", defaultValue = "") String uri,
                                      @RequestParam(defaultValue = "false") boolean unique) {
        return statsService.viewStats(start, end, uri, unique);
    }
}
