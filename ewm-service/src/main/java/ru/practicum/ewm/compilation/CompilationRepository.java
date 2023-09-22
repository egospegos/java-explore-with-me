package ru.practicum.ewm.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query(value = "select c.* from Compilations as c where c.pinned = true", nativeQuery = true)
    Page<Compilation> findAllWithPagination(Pageable pageable);

    @Query(value = "select c.* from Compilations as c ", nativeQuery = true)
    Page<Compilation> findAllPinnedWithPagination(Pageable pageable);
}
