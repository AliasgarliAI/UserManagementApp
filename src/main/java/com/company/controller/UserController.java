package com.company.controller;

import com.company.dto.HttpResponse;
import com.company.dto.UserDto;
import com.company.exception.domain.EmailNotFoundException;
import com.company.exception.domain.UserNotFoundException;
import com.company.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@SecurityRequirements({
        @SecurityRequirement(name = "bearerToken"),
        @SecurityRequirement(name = "basicAuth")
})
public class UserController {

    private static final HttpStatus statusOk = HttpStatus.OK;
    private final UserServiceImpl userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), statusOk);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUserById(userId), statusOk);
    }

    @GetMapping("/username/{userName}")
    public ResponseEntity<UserDto> getUserByUserName(@PathVariable String userName) {
        return new ResponseEntity<>(userService.getUserByUserName(userName), statusOk);
    }

    @GetMapping("/email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam("email") String email) throws EmailNotFoundException {
        return new ResponseEntity<>(userService.getUserByEmail(email), statusOk);
    }

    @PostMapping()
    public ResponseEntity<UserDto> saveNewUser(@RequestBody UserDto newUser) {
        return new ResponseEntity<>(userService.createUser(newUser), statusOk);
    }

    @PutMapping
    public ResponseEntity<UserDto> changeUser(@RequestBody UserDto updatedUser) {
        return new ResponseEntity<>(userService.updateUser(updatedUser), statusOk);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpResponse> removeUser(@PathVariable Long userId) {
        HttpResponse response = HttpResponse.builder()
                .httpStatus(statusOk).httpStatusCode(statusOk.value())
                .timeStamp(new Date()).message("Operation performed successfully").build();

        userService.deleteUser(userId);

        return new ResponseEntity<>(response, statusOk);
    }
}
