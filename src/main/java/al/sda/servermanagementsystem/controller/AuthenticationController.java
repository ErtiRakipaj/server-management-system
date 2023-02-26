package al.sda.servermanagementsystem.controller;

import al.sda.servermanagementsystem.requests.AuthenticationRequest;
import al.sda.servermanagementsystem.requests.AuthenticationResponse;
import al.sda.servermanagementsystem.requests.RegisterRequest;
import al.sda.servermanagementsystem.service.AuthenticationService;
import al.sda.servermanagementsystem.service.TokenBlacklistService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
            return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }
    @RequestMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            String jwtToken = request.getHeader("Authorization"); // obtain the JWT token from the request header
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                jwtToken = jwtToken.substring(7); // remove the "Bearer " prefix from the token
                authenticationService.logout(jwtToken);
                tokenBlacklistService.blacklistToken(jwtToken); // blacklist the JWT token
            }
            return ResponseEntity.ok("You have been logged out successfully.");
        } else {
            return ResponseEntity.badRequest().body("You are not currently logged in.");
        }
    }
}
