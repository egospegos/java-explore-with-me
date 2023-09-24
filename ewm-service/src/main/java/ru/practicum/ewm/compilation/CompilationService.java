package ru.practicum.ewm.compilation;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAllCompilationsPublic(boolean pinned, Long from, Long size);

    CompilationDto getCompilationByIdPublic(long compId);

    CompilationDto create(NewCompilationDto newCompilationDto);

    void delete(long compId);

    CompilationDto updateCompilationAdmin(long compId, UpdateCompilationRequest updateCompilationRequest);
}
