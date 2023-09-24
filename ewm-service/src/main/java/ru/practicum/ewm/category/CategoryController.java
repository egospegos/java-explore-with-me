package ru.practicum.ewm.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.util.common.Marker;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //ADMIN
    @PostMapping("/admin/categories")
    @Validated({Marker.OnCreate.class})
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.create(categoryDto), HttpStatus.CREATED);
    }


    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long catId) {
        categoryService.delete(catId);
    }

    @PatchMapping("/admin/categories/{catId}")
    @Validated({Marker.OnUpdate.class})
    public CategoryDto update(@PathVariable long catId, @RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.update(catId, categoryDto);
    }


    //PUBLIC
    @GetMapping("/categories")
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0") Long from,
                                    @RequestParam(defaultValue = "10") Long size) {
        return categoryService.getAll(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto get(@PathVariable long catId) {
        return categoryService.getById(catId);
    }

}
