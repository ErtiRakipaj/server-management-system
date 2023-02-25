package al.sda.servermanagementsystem.repository;

import al.sda.servermanagementsystem.model.Server;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ServerRepository extends JpaRepository <Server, Long> {
    Server findServerByIp(String ip);
    List<Server> findAllByOwner(String owner, PageRequest limit);
}
