package ru.practicum.ewm.category;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAll(Long from, Long size);

    CategoryDto getById(long id);

    CategoryDto create(CategoryDto categoryDto);

    CategoryDto update(long id, CategoryDto categoryDto);

    void delete(long id);
}
