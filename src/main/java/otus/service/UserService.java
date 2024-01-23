package otus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import otus.exception.EntityNotFoundException;
import otus.model.dto.UserDto;
import otus.model.entity.User;
import otus.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final ModelMapper mapper;

    public List<User> allUsers() {
        return repository.findAll();
    }

    public UUID createUser(UserDto dto) throws Exception {
        User existing = findUserByLogin(dto.getLogin());
        if (Objects.nonNull(existing)) {
            throw new Exception(String.format("User with login %s already exists", dto.getLogin()));
        }
        User user = mapper.map(dto, User.class);
        User savedUser = repository.save(user);
        log.info("Created new user uuid: {}, username: {}, password: {}",
                savedUser.getUserUUID(),
                savedUser.getLogin(),
                savedUser.getPassword());
        return savedUser.getUserUUID();
    }

    public void deleteUserById(int userId) {
        User user = findUserById(userId);
        repository.delete(user);
        log.info("User with id: {} has been deleted.", userId);
    }

    private User findUserById(Integer id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("User with id %s does not exist.", id)));
    }

    public User findUserByLogin(String login) {
        return repository.findByLogin(login);
    }

    public User findUserByLoginAndPassword(String login, String password) {
        return repository.findByLoginAndPassword(login, password).orElseThrow(() ->
                new EntityNotFoundException(String.format(
                        "User with login %s and password %s does not exist.", login, password)));
    }
}
