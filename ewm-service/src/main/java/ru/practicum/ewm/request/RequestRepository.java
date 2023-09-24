package ru.practicum.ewm.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query(value = "select r.* from Requests as r " +
            "where r.id = ?1 AND r.requester_id = ?2", nativeQuery = true)
    Request findByIdAndUserId(long requestId, long userId);

    @Query(value = "select r.* from Requests as r " +
            "where r.requester_id = ?1", nativeQuery = true)
    List<Request> findAllByUserId(long userId);

    @Query(value = "select COUNT(r.id) from Requests as r " +
            "where r.event_id = ?1 AND r.status = 'CONFIRMED' " +
            "group by r.id", nativeQuery = true)
    Long countConfirmedRequestsByEventId(long eventId);

    @Query(value = "select COUNT(r.id) from Requests as r " +
            "where r.event_id = ?1 " +
            "group by r.id", nativeQuery = true)
    Long countRequestsByEventId(long eventId);

    @Query(value = "select r.* from Requests as r " +
            "where r.event_id = ?1 AND r.requester_id = ?2", nativeQuery = true)
    Request findByEventIdAndUserId(long eventId, long userId);

    @Query(value = "select r.* from Requests as r " +
            "where r.event_id = ?1 ", nativeQuery = true)
    List<Request> findAllByEventId(long eventId);
}
