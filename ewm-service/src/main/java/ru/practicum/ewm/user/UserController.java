package ru.practicum.ewm.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.util.common.Marker;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAll(@RequestParam(name = "ids", defaultValue = "") List<Integer> ids,
                                @RequestParam(defaultValue = "0") Long from,
                                @RequestParam(defaultValue = "10") Long size) {
        return userService.getAll(ids, from, size);
    }


    @PostMapping
    @Validated({Marker.OnCreate.class})
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(userService.create(userDto), HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }
}
