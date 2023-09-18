package ru.practicum.ewm.user;

import java.util.List;

public interface UserService {
    List<UserDto> getAll(List<Integer> ids, Long from, Long size);

    UserDto create(UserDto userDto);


    void delete(long userId);
}
