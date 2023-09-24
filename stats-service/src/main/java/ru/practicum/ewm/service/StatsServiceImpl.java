package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.EndpointHitMapper;
import ru.practicum.ewm.dto.IViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public EndpointHitDto addHit(EndpointHitDto endpointHitDto) {
        //маппинг
        EndpointHitMapper mapper = Mappers.getMapper(EndpointHitMapper.class);
        EndpointHit endpointHit = mapper.dtoToEndpointHit(endpointHitDto);

        //маппинг перед отправкой
        return mapper.endpointHitToDto(statsRepository.save(endpointHit));
    }

    @Override
    public List<IViewStats> viewStats(LocalDateTime start, LocalDateTime end, List<String> uri, boolean unique) {
        if (start.isAfter(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start is after end");
        }
        if (unique) {
            if (uri.isEmpty()) {
                return statsRepository.findAllWithUniqueIp(start, end);
            } else {
                return statsRepository.findByUriWithUniqueIp(start, end, uri);
            }
        } else {
            if (uri.isEmpty()) {
                return statsRepository.findAllWithNonUniqueIp(start, end);
            } else {
                return statsRepository.findByUriWithNonUniqueIp(start, end, uri);
            }
        }

    }
}
