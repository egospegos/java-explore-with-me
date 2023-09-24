package ru.practicum.ewm.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@Validated
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    //PRIVATE-------------------------------------------------------------

    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> getAllRequestsPrivate(@PathVariable long userId) {
        return requestService.getAllRequestsPrivate(userId);
    }

    @PostMapping("/users/{userId}/requests")
    public ResponseEntity<ParticipationRequestDto> createRequestPrivate(@PathVariable long userId,
                                                                        @RequestParam(required = true) long eventId) {
        return new ResponseEntity<>(requestService.createRequestPrivate(userId, eventId), HttpStatus.CREATED);
    }


    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto updateRequestPrivate(@PathVariable long userId, @PathVariable long requestId) {
        return requestService.updateRequestPrivate(userId, requestId);
    }

}
