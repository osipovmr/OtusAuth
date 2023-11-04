package otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import otus.model.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
