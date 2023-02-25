package al.sda.servermanagementsystem.service;

import al.sda.servermanagementsystem.enums.Status;
import al.sda.servermanagementsystem.model.Server;
import al.sda.servermanagementsystem.repository.ServerRepository;
import al.sda.servermanagementsystem.requests.CreateServerRequest;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.AsnResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.EnterpriseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ServerService {

    private final ServerRepository serverRepository;

    public List<Server> getAllServer(int limit) {
        log.info("Getting all servers");
        return serverRepository.findAllByOwner(extractUsernameFromTheCurrentLoggedUser(),PageRequest.of(0,limit));
    }

    public Server findById(Long id) {
        log.info("Getting server with ID: {}",id);
        return serverRepository.findById(id).get();
    }
    public Server createNewServer(CreateServerRequest request) {
        log.info("Creating new server");
        Server server = Server
                .builder()
                .ip(request.getIp())
                .imageUrl(setServerImageUrl())
                .owner(extractUsernameFromTheCurrentLoggedUser())
                .build();
        return serverRepository.save(server);
    }

    public Boolean deleteServerById(Long id) {
        log.info("Deleting server with id: {}",id);
        serverRepository.deleteById(id);
        return TRUE;
    }

    public Server updateServer(CreateServerRequest request){
        log.info("Updating server: {}",request.getIp());
        Server server = Server
                .builder()
                .ip(request.getIp())
                .imageUrl(setServerImageUrl())
                .owner(extractUsernameFromTheCurrentLoggedUser())
                .build();
        return serverRepository.save(server);
    }


    public Server pingServer(String ip) throws IOException, GeoIp2Exception {
        Server server = serverRepository.findServerByIp(ip);

        InetAddress address = InetAddress.getByName(ip);

        server.setLocation(locationExtractor(address));

        server.setCompanyName(enterpriseExtractor(address));

        server.setStatus(address.isReachable(10000)? Status.ACTIVE: Status.INACTIVE);

        serverRepository.save(server);

        return server;
    }



    private String setServerImageUrl(){
        String[] imageURLs = {"server1.png","server2.png","server3.png","server4.png"};

        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/servers/image/"+imageURLs[new Random().nextInt(4)]).toUriString();
    }



    private boolean isReachable(String ipAddress, int port, int timeOut) {
        try {
            try(Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(ipAddress, port), timeOut);
            }
            return true;
        }catch (IOException exception){
            return false;
        }
    }

    private String extractUsernameFromTheCurrentLoggedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

    private DatabaseReader locationDatabase() throws IOException {
        File database = new File("C:\\Users\\user\\Documents\\SDAFinalProject\\GeoLite2-City_20230224\\GeoLite2-City.mmdb");
        DatabaseReader reader = new DatabaseReader.Builder(database).build();

        return reader;
    }
    private DatabaseReader locationASNDatabase() throws IOException {
        File database = new File("C:\\Users\\user\\Documents\\SDAFinalProject\\GeoLite2-ASN_20230224\\GeoLite2-ASN.mmdb");
        DatabaseReader reader = new DatabaseReader.Builder(database).build();

        return reader;
    }

    private String locationExtractor(InetAddress ip) throws IOException, GeoIp2Exception {
        DatabaseReader locationDb = locationDatabase();
        CityResponse cityResponse = locationDb.city(ip);

        String city = cityResponse.getCity().getName();
        String country = cityResponse.getCountry().getName();

        return country+", "+city;
    }

    private String enterpriseExtractor(InetAddress ip) throws IOException, GeoIp2Exception {

        DatabaseReader locationASNDb = locationASNDatabase();
        AsnResponse response = locationASNDb.asn(ip);

        return response.getAutonomousSystemOrganization();

    }
}

