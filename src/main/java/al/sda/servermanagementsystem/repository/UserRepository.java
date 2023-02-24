package al.sda.servermanagementsystem.repository;

import al.sda.servermanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findUserByUsername(String username);
}
