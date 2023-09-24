package ru.practicum.ewm.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "select e.* from Events as e where e.category_id = ?1", nativeQuery = true)
    List<Event> findAllEventsByCategoryId(long id);

    @Query(value = "select e.* from Events as e where e.initiator_id = ?1", nativeQuery = true)
    Page<Event> findAllEventsByUserIdPrivate(long userId, Pageable pageable);

    @Query(value = "select e.* from Events as e where e.id = ?1 AND e.initiator_id = ?2", nativeQuery = true)
    Event findByIdAndUserId(long eventId, long userId);

    @Query(value = "select e.* from Events as e" +
            " where e.initiator_id IN (?1) AND e.state IN (?2) AND e.category_id IN (?3)" +
            " AND e.event_date >= ?4 AND e.event_date <= ?5", nativeQuery = true)
    Page<Event> findAllEventsAdmin(List<Integer> users, List<String> states, List<Integer> categories,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "select e.* from Events as e", nativeQuery = true)
    Page<Event> findAllEventsAdminWithoutParams(Pageable pageable);

    @Query(value = "select e.id from Events as e where e.compilation_id = ?1 ", nativeQuery = true)
    List<Long> findEventIdsByCompilationId(long compId);


    @Query(value = "select e.* from Events as e" +
            " where e.category_id IN (?1) AND e.state = 'PUBLISHED'" +
            " AND e.event_date > TO_CHAR(now(), 'yyyy-MM-dd HH:mm:ss')", nativeQuery = true)
    Page<Event> findAllEventsPublicByCategories(List<Integer> categories, Pageable pageable);

    @Query(value = "select e.* from Events as e" +
            " where e.state = 'PUBLISHED'" +
            " AND e.event_date > TO_CHAR(now(), 'yyyy-MM-dd HH:mm:ss')", nativeQuery = true)
    Page<Event> findAllWithPagination(Pageable pageable);

    @Query(value = "select e.* from Events as e" +
            " where e.category_id IN (?2) AND e.paid = ?3 AND e.state = 'PUBLISHED' AND e.event_date > TO_CHAR(now(), 'yyyy-MM-dd HH:mm:ss') AND" +
            " (upper(e.annotation) like upper(concat('%', ?1, '%')) OR upper(e.description) like upper(concat('%', ?1, '%')))", nativeQuery = true)
    Page<Event> findAllEventsPublicWithoutRangeDate(String text, List<Integer> categories, Boolean paid, Pageable pageable);

    @Query(value = "select e.* from Events as e" +
            " where e.category_id IN (?2) AND e.paid = ?3 AND e.state = 'PUBLISHED' AND e.event_date > ?4 AND e.event_date <= ?5 AND" +
            " (upper(e.annotation) like upper(concat('%', ?1, '%')) OR upper(e.description) like upper(concat('%', ?1, '%')))", nativeQuery = true)
    Page<Event> findAllEventsPublicWithRangeDate(String text, List<Integer> categories, Boolean paid,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "select e.* from Events as e" +
            " where e.id IN (?1) ", nativeQuery = true)
    List<Event> findAllEventsByIds(List<Long> ids);
}
