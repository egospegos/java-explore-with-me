package ru.practicum.ewm.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.IViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    EndpointHit save(EndpointHit endpointHit);

    @Query(value = "select h.app as app, h.uri as uri, COUNT(distinct h.ip) as hits " +
            "from hits as h " +
            "where h.created >= ?1 AND h.created <= ?2 " +
            "group by h.app, h.uri", nativeQuery = true)
    List<IViewStats> findAllWithUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query(value = "select h.app as app, h.uri as uri, COUNT(distinct h.ip) as hits " +
            "from hits as h " +
            "where h.uri IN (?3) AND h.created >= ?1 AND h.created <= ?2 " +
            "group by h.app, h.uri " +
            "order by hits DESC", nativeQuery = true)
    List<IViewStats> findByUriWithUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uri);

    @Query(value = "select h.app as app, h.uri as uri, COUNT(h.ip) as hits " +
            "from hits as h " +
            "where h.created >= ?1 AND h.created <= ?2 " +
            "group by h.app, h.uri", nativeQuery = true)
    List<IViewStats> findAllWithNonUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query(value = "select h.app as app, h.uri as uri, COUNT(h.ip) as hits " +
            "from hits as h " +
            "where h.uri IN (?3) AND h.created >= ?1 AND h.created <= ?2 " +
            "group by h.app, h.uri " +
            "order by hits DESC", nativeQuery = true)
    List<IViewStats> findByUriWithNonUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uri);
}
