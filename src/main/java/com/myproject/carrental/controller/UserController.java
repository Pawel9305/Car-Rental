package com.myproject.carrental.controller;

import com.myproject.carrental.domain.UserDto;
import com.myproject.carrental.exception.UserNotFoundException;
import com.myproject.carrental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createUser(@RequestBody UserDto userDto) {
        userService.addUser(userDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUser(@RequestBody UserDto userDto) {
        userService.editUser(userDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") long id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }
}
