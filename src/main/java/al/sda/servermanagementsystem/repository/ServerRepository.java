package al.sda.servermanagementsystem.repository;

import al.sda.servermanagementsystem.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository <Server, Long> {
}
