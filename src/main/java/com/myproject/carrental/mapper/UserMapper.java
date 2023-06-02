package com.myproject.carrental.mapper;

import com.myproject.carrental.domain.User;
import com.myproject.carrental.domain.UserDto;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class UserMapper {

    @Autowired
    private RentalMapper rentalMapper;

    public User mapToUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getSurname(),
                userDto.getIdNumber(),
                List.of(),
                userDto.getEmail()
        );
    }

    public UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getIdNumber(),
                user.getEmail(),
                rentalMapper.mapToRentalDtoList(user.getRentals())
        );
    }

    public List<UserDto> mapToUserDtoList(List<User> users) {
        return users.stream()
                .map(this::mapToUserDto)
                .toList();
    }
}
