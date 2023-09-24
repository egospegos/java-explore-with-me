package ru.practicum.ewm.category;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.DublicateException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<CategoryDto> getAll(Long from, Long size) {

        List<Category> categories = new ArrayList<>();
        long start = from / size;

        categories = categoryRepository.findAllWithPagination(PageRequest.of(Math.toIntExact(start), Math.toIntExact(size),
                Sort.by("id").ascending())).getContent();

        CategoryMapper mapper = Mappers.getMapper(CategoryMapper.class);
        List<CategoryDto> categoriesDto = new ArrayList<>();
        for (Category category : categories) {
            CategoryDto categoryDto = mapper.categoryToCategoryDto(category);
            categoriesDto.add(categoryDto);

        }
        return categoriesDto;
    }

    @Override
    public CategoryDto getById(long id) {
        CategoryMapper mapper = Mappers.getMapper(CategoryMapper.class);
        Category actual = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Wrong category id=" + id));
        return mapper.categoryToCategoryDto(actual);
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        //маппинг
        CategoryMapper mapper = Mappers.getMapper(CategoryMapper.class);
        Category category = mapper.categoryDtoToCategory(categoryDto);
        try {
            Category categoryFromRepo = categoryRepository.save(category);
            return mapper.categoryToCategoryDto(categoryFromRepo);
        } catch (DataIntegrityViolationException e) {
            throw new DublicateException(String.format(
                    "Категория %s уже зарегистрирована", category.getName()
            ));
        }
    }

    @Override
    public CategoryDto update(long id, CategoryDto categoryDto) {
        validateId(id);
        CategoryMapper mapper = Mappers.getMapper(CategoryMapper.class);
        Category category = mapper.categoryDtoToCategory(categoryDto);
        category.setId(categoryRepository.findById(id).get().getId());


        try {
            return mapper.categoryToCategoryDto(categoryRepository.save(category));
        } catch (DataIntegrityViolationException e) {
            throw new DublicateException(String.format(
                    "Категория %s уже зарегистрирована", category.getName()
            ));
        }
    }

    @Override
    public void delete(long id) {
        validateId(id);
        List<Event> events = eventRepository.findAllEventsByCategoryId(id);
        if (events.size() > 0) {
            throw new DublicateException("У категории есть привязанные события");
        }

        categoryRepository.deleteById(id);
    }

    private void validateId(Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new IllegalStateException("Wrong category id=" + catId));
    }
}
