package ru.practicum.ewm.category;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CategoryMapper {
    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "name", source = "entity.name")
    Category categoryDtoToCategory(CategoryDto entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "name", source = "entity.name")
    CategoryDto categoryToCategoryDto(Category entity);
}
