package ru.practicum.ewm.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);

    void deleteById(long userId);

    @Query(value = "select u.id, u.name, u.email from Users as u ", nativeQuery = true)
    Page<User> findAllWithPagination(Pageable pageable);

    @Query(value = "select u.id, u.name, u.email from Users as u " +
            "where u.id IN (?1) ", nativeQuery = true)
    Page<User> findByIdsWithPagination(List<Integer> ids, Pageable pageable);
}
