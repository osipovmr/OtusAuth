package otus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import otus.exception.EntityNotFoundException;
import otus.model.dto.UserDto;
import otus.model.dto.UserInfoDto;
import otus.model.dto.UserUpdateDto;
import otus.model.entity.User;
import otus.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final ModelMapper mapper;

    public UserInfoDto getUserById(Integer id) {
        User user = findUserById(id);
        return mapper.map(user, UserInfoDto.class);
    }

    public void createUser(UserDto dto) {
        User user = mapper.map(dto, User.class);
        User savedUser = repository.save(user);
        log.info("Created new user id: {}, username: {}.", savedUser.getId(), savedUser.getUsername());
    }

    public void deleteUserById(int userId) {
        User user = findUserById(userId);
        repository.delete(user);
        log.info("User with id: {} has been deleted.", userId);
    }

    public void updateUser(int userId, UserUpdateDto dto) {
        User user = findUserById(userId);
        updateFromDto(dto, user);
        repository.save(user);
        log.info("User {} has been updated.", userId);
    }

    private User findUserById(Integer id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("User with id %s does not exist.", id)));
    }

    private void updateFromDto(UserUpdateDto dto, User user) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
    }
}
