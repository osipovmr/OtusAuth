package otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import otus.model.dto.LoginRequest;
import otus.model.dto.UserAuthResponseDto;
import otus.model.dto.UserDto;
import otus.model.entity.User;
import otus.service.UserService;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final HashMap<UUID, User> sessions = new HashMap<>();

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto dto) {
        UUID newUserId;
        try {
            newUserId = userService.createUser(dto);
            kafkaTemplate.send("newUser", String.valueOf(newUserId));
            log.info("Отправлен запрос на создание счета для нового пользователя {}.", newUserId);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(String.format("REGISTER NEW USER WITH ID: %s.", newUserId), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user;
        try {
            user = userService.findUserByLoginAndPassword(loginRequest.getLogin(), loginRequest.getPassword());
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    String.format("User with login %s does not exist. Please go to '/register' page", loginRequest.getLogin()),
                    HttpStatus.UNAUTHORIZED);
        }
        UUID sessionUUID = createSession(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "sessionUUID=" + sessionUUID);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/auth")
    public ResponseEntity<?> auth(@CookieValue(name = "sessionUUID", required = false) UUID sessionUUID) {
        if (sessionUUID == null) {
            log.error("sessionUUID == null");
            return new ResponseEntity<>("AUTHORIZATION IS REQUIRED", HttpStatus.UNAUTHORIZED);
        }
        User user = sessions.get(sessionUUID);
        if (Objects.isNull(user)) {
            log.error("No user for sessoinUUID = {}", sessionUUID);
            return new ResponseEntity<>("AUTHORIZATION IS REQUIRED", HttpStatus.UNAUTHORIZED);
        } else {
            HttpHeaders headers = getHttpHeaders(user);
            UserAuthResponseDto response = UserAuthResponseDto.builder()
                    .userUUID(user.getUserUUID())
                    .login(user.getLogin())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .build();
            log.info("Аутентификация для пользователя {}.", user.getUserUUID());
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        }
    }

    @RequestMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(name = "sessionUUID") UUID sessionUUID) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "sessionUUID=");
        log.info("Пользователь {} вышел из системы.", sessions.get(sessionUUID).getUserUUID());
        sessions.remove(sessionUUID);
        return new ResponseEntity<>("LOGOUT SUCCESSFULLY", headers, HttpStatus.OK);
    }

    @GetMapping("/getSessions")
    public ResponseEntity<HashMap<UUID, User>> getSessions(@CookieValue(name = "sessionUUID", required = false) UUID sessionUUID) {
        if (sessions.containsKey(sessionUUID))
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/signin")
    public ResponseEntity<String> signin() {
        log.info("Совершен не авторизованный запрос.");
        return new ResponseEntity<>("Please go to '/login' and provide Login/Password", HttpStatus.UNAUTHORIZED);
    }

    private HttpHeaders getHttpHeaders(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-UserId", user.getUserUUID().toString());
        headers.add("X-User", user.getLogin());
        headers.add("X-Email", user.getEmail());
        headers.add("X-First-Name", user.getFirstName());
        headers.add("X-Last-Name", user.getLastName());
        return headers;
    }

    private UUID createSession(User user) {
        UUID sessionUUID = UUID.randomUUID();
        sessions.put(sessionUUID, user);
        return sessionUUID;
    }
}
