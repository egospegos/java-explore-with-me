package ru.practicum.ewm.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.util.common.Marker;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
public class CompilationController {

    private final CompilationService compilationService;

    @Autowired
    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    //PUBLIC-------------------------------------------------------------

    @GetMapping("/compilations")
    public List<CompilationDto> getAllCompilationsPublic(@RequestParam(defaultValue = "false") boolean pinned,
                                                         @RequestParam(defaultValue = "0") Long from,
                                                         @RequestParam(defaultValue = "10") Long size) {
        return compilationService.getAllCompilationsPublic(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationByIdPublic(@PathVariable long compId) {
        return compilationService.getCompilationByIdPublic(compId);
    }


    //ADMIN-------------------------------------------------------------

    @PostMapping("/admin/compilations")
    @Validated({Marker.OnCreate.class})
    public ResponseEntity<CompilationDto> create(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return new ResponseEntity<>(compilationService.create(newCompilationDto), HttpStatus.CREATED);
    }


    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long compId) {
        compilationService.delete(compId);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilationAdmin(@PathVariable long compId, @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        return compilationService.updateCompilationAdmin(compId, updateCompilationRequest);
    }

}
