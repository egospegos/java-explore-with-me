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
}
