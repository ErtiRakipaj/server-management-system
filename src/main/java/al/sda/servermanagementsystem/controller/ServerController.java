package al.sda.servermanagementsystem.controller;

import al.sda.servermanagementsystem.enums.Status;
import al.sda.servermanagementsystem.model.Response;
import al.sda.servermanagementsystem.model.Server;
import al.sda.servermanagementsystem.requests.CreateServerRequest;
import al.sda.servermanagementsystem.service.ServerService;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
@CrossOrigin
public class ServerController {

    private final ServerService serverService;
    @GetMapping("")
        public ResponseEntity<Response> getServers() {

            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("servers",serverService.getAllServer(30)))
                            .message("Servers Retrieved")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        }


    @PostMapping("/save")
    public ResponseEntity<Response> saveServer(@RequestBody @Valid CreateServerRequest serverRequest) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("server", serverService.createNewServer(serverRequest)))
                        .message("Server Created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException, GeoIp2Exception {
        Server server = serverService.pingServer(ipAddress);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("server", server))
                        .message(server.getStatus() == Status.ACTIVE ? "Ping Success" : "Ping Failed")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
