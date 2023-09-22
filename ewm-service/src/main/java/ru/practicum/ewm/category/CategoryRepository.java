package ru.practicum.ewm.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category save(Category category);

    void deleteById(long catId);

    @Query(value = "select c.id, c.name from Categories as c ", nativeQuery = true)
    Page<Category> findAllWithPagination(Pageable pageable);
}
