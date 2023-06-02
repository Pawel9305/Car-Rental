package com.myproject.carrental.service;

import com.myproject.carrental.exception.UserNotFoundException;
import com.myproject.carrental.domain.User;
import com.myproject.carrental.domain.UserDto;
import com.myproject.carrental.mapper.UserMapper;
import com.myproject.carrental.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTestSuite {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserMapper userMapper;

    @Test
    void testUserDbServiceAddUser() {
        //Given
        UserDto userDto = new UserDto("testName", "testSurname", "12345678", "test@email.com");
        User user = new User(1, "testName", "testSurname", "12345678", List.of(), "test@email.com");
        when(userMapper.mapToUser(userDto)).thenReturn(user);

        //When
        userService.addUser(userDto);

        //Then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUserDbServiceFindAllUsersFetchEmptyList() {
        //Given
        when(userMapper.mapToUserDtoList(anyList())).thenReturn(List.of());

        //When
        List<UserDto> usersFound = userService.findAllUsers();

        //Then
        verify(userRepository, times(1)).findAll();
        assertEquals(0, usersFound.size());
    }

    @Test
    void testUserDbServiceFindAllUsers() {
        //Given
        when(userMapper.mapToUserDtoList(any())).thenReturn(List.of(new UserDto("test_name", "test_name", "test_id", "test_email")));

        //When
        List<UserDto> usersFound = userService.findAllUsers();

        //Then
        verify(userRepository, times(1)).findAll();
        assertEquals(1, usersFound.size());
        assertEquals("test_id", usersFound.get(0).getIdNumber());
    }

    @Test
    void testUserDbServiceEditUser() {
        //Given
        User user = new User(1, "testName", "testSurname", "12345678", List.of(), "test@email.com");
        UserDto userDto = new UserDto(1, "newTestName", "testSurname", "12345678", "test@email.com", List.of());
        when(userMapper.mapToUser(userDto)).thenReturn(user);

        //When
        userService.editUser(userDto);

        //Then
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUserDbServiceDeleteById() {
        //Given
        //When
        userService.deleteUser(1);

        //Then
        verify(userRepository, times(1)).deleteById(any());
    }

    //TODO: test that method
    @Test
    void testUserDbServiceGetById() throws UserNotFoundException {
        //Given
        User user = new User(1L, "testName", "testSurname", "12345678", List.of(), "test@email.com");
        UserDto userDto = new UserDto(1L, "testName", "testSurname", "12345678", "test@email.com", List.of());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.mapToUserDto(user)).thenReturn(userDto);

        //When
        UserDto resultUser = userService.getById(1);

        //Then
        verify(userRepository, times(1)).findById(1);
        assertEquals(1, resultUser.getId());
    }

    @Test
    void testUserDbServiceGetByIdThrowException() throws UserNotFoundException {
        //Given
        //When
        //Then
        assertThrows(UserNotFoundException.class, () -> userService.getById(anyLong()));
    }
}
