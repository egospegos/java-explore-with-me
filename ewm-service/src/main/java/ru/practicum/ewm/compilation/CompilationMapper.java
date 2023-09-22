package ru.practicum.ewm.compilation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;

@Mapper
public interface CompilationMapper {

    @Mapping(target = "pinned", source = "entity.pinned")
    @Mapping(target = "title", source = "entity.title")
    Compilation newCompilationDtoToCompilation(NewCompilationDto entity);

    @Mapping(target = "pinned", source = "entity.pinned")
    @Mapping(target = "title", source = "entity.title")
    CompilationDto compilationToCompilationDto(Compilation entity);

    @Mapping(target = "pinned", source = "entity.pinned")
    @Mapping(target = "title", source = "entity.title")
    CompilationDto UpdateCompilationRequestToCompilationDto(UpdateCompilationRequest entity);
}
