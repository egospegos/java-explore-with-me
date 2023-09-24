package ru.practicum.ewm.user;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.DublicateException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAll(List<Integer> ids, Long from, Long size) {

        List<User> users = new ArrayList<>();
        long start = from / size;
        if (ids.isEmpty()) {
            users = userRepository.findAllWithPagination(PageRequest.of(Math.toIntExact(start), Math.toIntExact(size),
                    Sort.by("id").ascending())).getContent();
        } else {
            users = userRepository.findByIdsWithPagination(ids, PageRequest.of(Math.toIntExact(start), Math.toIntExact(size),
                    Sort.by("id").ascending())).getContent();
        }
        UserMapper mapper = Mappers.getMapper(UserMapper.class);
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = mapper.userToUserDto(user);
            usersDto.add(userDto);

        }
        return usersDto;
    }

    @Override
    public UserDto create(UserDto userDto) {
        //маппинг
        UserMapper mapper = Mappers.getMapper(UserMapper.class);
        User user = mapper.userDtoToUser(userDto);
        try {
            return mapper.userToUserDto(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new DublicateException(String.format(
                    "Пользователь с %s уже зарегистрирован", user.getEmail()
            ));
        }
    }

    @Override
    public void delete(long userId) {
        validateId(userId);
        userRepository.deleteById(userId);
    }

    private void validateId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Wrong user id=" + userId));
    }
}
