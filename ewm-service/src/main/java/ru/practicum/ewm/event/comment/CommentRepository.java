package ru.practicum.ewm.event.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    @Query(value = " select c.id, c.text, c.event_id, c.author_id, c.created from Comments as c " +
            "where c.event_id = ?1 ", nativeQuery = true)
    List<Comment> findAllByEventId(long eventId);
}
