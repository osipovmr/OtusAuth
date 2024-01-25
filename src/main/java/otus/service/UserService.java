package otus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import otus.model.dto.UserDto;
import otus.model.entity.User;
import otus.repository.UserRepository;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;

    public UUID createUser(UserDto dto) throws Exception {
        User existing = findUserByLogin(dto.getLogin());
        if (Objects.nonNull(existing)) {
            throw new Exception(String.format("User with login %s already exists", dto.getLogin()));
        }
        User user = User.builder()
                .login(dto.getLogin())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();
        User savedUser = repository.save(user);
        log.info("Created new user uuid: {}, username: {}, password: {}",
                savedUser.getUserUUID(),
                savedUser.getLogin(),
                savedUser.getPassword());
        return savedUser.getUserUUID();
    }

    public User findUserByLogin(String login) {
        return repository.findByLogin(login);
    }

    public User findUserByLoginAndPassword(String login, String password) {
        return repository.findByLoginAndPassword(login, password).orElseThrow();
    }
}
