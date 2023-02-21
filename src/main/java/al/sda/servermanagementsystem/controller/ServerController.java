package al.sda.servermanagementsystem.controller;

import al.sda.servermanagementsystem.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ServerController {

    private final ServerService serverService;
}
