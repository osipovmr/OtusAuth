package otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import otus.model.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByLogin(String username);

    Optional<User> findByLoginAndPassword(String login, String password);
}
