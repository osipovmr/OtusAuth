package otus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import otus.model.dto.UserDto;
import otus.model.entity.User;
import otus.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UUID createUser(UserDto dto) throws Exception {
        Optional<User> existing = findUserByLogin(dto.getLogin());
        if (existing.isPresent()) {
            log.error("Пользователь с таким логином уже есть.");
            throw new Exception(String.format("User with login %s already exists", dto.getLogin()));
        }
        log.info("Создание нового пользователя.");
        User user = User.builder()
                .login(dto.getLogin())
                .password(passwordEncoder.encode(dto.getPassword()))
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

    public Optional<User> findUserByLogin(String login) {
        return repository.findByLogin(login);
    }

    public User findUserByLoginAndPassword(String login, String password) {
        Optional<User> optionalUser = findUserByLogin(login);
        if (optionalUser.isPresent() && passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            return optionalUser.get();
        } else {
            throw new NoSuchElementException();
        }
    }
}
