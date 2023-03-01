package al.sda.servermanagementsystem.controller;

import al.sda.servermanagementsystem.enums.Status;
import al.sda.servermanagementsystem.model.Response;
import al.sda.servermanagementsystem.model.Server;
import al.sda.servermanagementsystem.requests.CreateServerRequest;
import al.sda.servermanagementsystem.service.ServerService;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class ServerController {

    private final ServerService serverService;
    @GetMapping("")
        public ResponseEntity<Response> getServers() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
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

        try {
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
        } catch (AddressNotFoundException anfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .timeStamp(now())
                            .message("Address not found")
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .build());
        } catch (IOException | GeoIp2Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.builder()
                            .timeStamp(now())
                            .message("Internal server error")
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build());
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id) {
        Boolean isServerDeleted = serverService.deleteServer(id);
        HttpStatus status = isServerDeleted.booleanValue() == Boolean.TRUE ? OK : BAD_REQUEST;
        String message = isServerDeleted.booleanValue() == Boolean.TRUE ? "Deleted Successfully" : "Deletion failed";
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("deleted",isServerDeleted))
                        .message(message)
                        .status(status)
                        .statusCode(status.value())
                        .build()
        );
    }
}
