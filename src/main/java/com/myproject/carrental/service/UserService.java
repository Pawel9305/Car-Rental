package com.myproject.carrental.service;

import com.myproject.carrental.domain.UserDto;
import com.myproject.carrental.exception.UserNotFoundException;
import com.myproject.carrental.mapper.UserMapper;
import com.myproject.carrental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    private final UserMapper userMapper;

    public void addUser(UserDto userDto) {
        repository.save(userMapper.mapToUser(userDto));
    }

    public List<UserDto> findAllUsers() {
        return userMapper.mapToUserDtoList(repository.findAll());
    }

    public void editUser(UserDto userDto) {
        repository.save(userMapper.mapToUser(userDto));
    }

    public void deleteUser(long id) {
        repository.deleteById(id);
    }

    public UserDto getById(long id) throws UserNotFoundException {
        return userMapper.mapToUserDto(repository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found")));
    }
}
