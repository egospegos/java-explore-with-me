package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    private final CompilationMapper compilationMapper = Mappers.getMapper(CompilationMapper.class);
    private final EventMapper eventMapper = Mappers.getMapper(EventMapper.class);

    @Override
    public List<CompilationDto> getAllCompilationsPublic(boolean pinned, Long from, Long size) {
        List<Compilation> compilations = new ArrayList<>();
        long start = from / size;
        if (pinned) {
            compilations = compilationRepository.findAllPinnedWithPagination(PageRequest.of(Math.toIntExact(start), Math.toIntExact(size),
                    Sort.by("id").ascending())).getContent();
        } else {
            compilations = compilationRepository.findAllWithPagination(PageRequest.of(Math.toIntExact(start), Math.toIntExact(size),
                    Sort.by("id").ascending())).getContent();
        }

        List<CompilationDto> dtos = new ArrayList<>();
        for (Compilation compilation : compilations) {
            CompilationDto compilationDto = compilationMapper.compilationToCompilationDto(compilation);
            List<Long> eventIds = eventRepository.findEventIdsByCompilationId(compilation.getId());
            for (Long id : eventIds) {
                Event event = eventRepository.findById(id)
                        .orElseThrow(() -> new IllegalStateException("Wrong event id=" + id));
                compilationDto.getEvents().add(eventMapper.eventToEventShortDto(event));
            }
            dtos.add(compilationDto);
        }

        return dtos;
    }


    @Override
    public CompilationDto getCompilationByIdPublic(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new IllegalStateException("Wrong compilation id=" + compId));
        List<Long> eventIds = eventRepository.findEventIdsByCompilationId(compilation.getId());
        CompilationDto compilationDto = compilationMapper.compilationToCompilationDto(compilation);
        for (Long id : eventIds) {
            Event event = eventRepository.findById(id)
                    .orElseThrow(() -> new IllegalStateException("Wrong event id=" + id));
            compilationDto.getEvents().add(eventMapper.eventToEventShortDto(event));
        }
        return compilationDto;
    }

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getPinned() == null) {
            newCompilationDto.setPinned(false);
        }
        Compilation compilation = compilationRepository.save(compilationMapper.newCompilationDtoToCompilation(newCompilationDto));
        CompilationDto compilationDto = compilationMapper.compilationToCompilationDto(compilation);
        if (newCompilationDto.getEvents() != null) {
            for (Long eventId : newCompilationDto.getEvents()) {
                Event event = eventRepository.findById(eventId)
                        .orElseThrow(() -> new IllegalStateException("Wrong event id=" + eventId));
                event.setCompilation(compilation);
                eventRepository.save(event);
                compilationDto.getEvents().add(eventMapper.eventToEventShortDto(event));
            }
        }

        return compilationDto;
    }

    @Override
    public void delete(long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilationAdmin(long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new IllegalStateException("Wrong compilation id=" + compId));
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllEventsByIds(updateCompilationRequest.getEvents());
            for (Event event : events) {
                event.setCompilation(compilation);
                eventRepository.save(event);
            }
        }
        return compilationMapper.compilationToCompilationDto(compilationRepository.save(compilation));
    }
}
