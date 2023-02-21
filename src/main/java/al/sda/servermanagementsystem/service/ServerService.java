package al.sda.servermanagementsystem.service;

import al.sda.servermanagementsystem.model.Server;
import al.sda.servermanagementsystem.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
@Transactional
public class ServerService {

    private final ServerRepository serverRepository;

    public List<Server> getAllServer(Server server) {
        return serverRepository.findAll();
    }
    public Server findByIp(String ip) {
        return serverRepository.findServerByIp(ip).orElseThrow(
                ()-> new RuntimeException("Server with " + ip + " not found")
        );
    }
    public Server findById(Long id) {
        return serverRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Server with " + id + " not found")
        );
    }
    public Server createNewServer(Server server) {
        //TODO add image url
        return serverRepository.save(server);
    }

    public Boolean deleteServerById(Long id) {
        serverRepository.deleteById(id);
        return TRUE;
    }
    public Server updateServer(Server server){
        return serverRepository.save(server);
    }
}
