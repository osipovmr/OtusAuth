package otus.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import otus.model.dto.UserDto;
import otus.model.dto.UserInfoDto;
import otus.model.dto.UserUpdateDto;
import otus.service.UserService;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoDto> getUserById(@PathVariable("id") int userId) {
        log.info("Get request for user {}.", userId);
        if ((LocalDateTime.now().getMinute()%2) == 0) {
            return ResponseEntity.ok(userService.getUserById(userId));
        }
       else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDto dto) {
        userService.createUser(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") int userId) {
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUserById(@PathVariable("id") int userId, @Valid @RequestBody UserUpdateDto dto) {
        userService.updateUser(userId, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
