package al.sda.servermanagementsystem.service;

import al.sda.servermanagementsystem.model.User;
import al.sda.servermanagementsystem.repository.UserRepository;
import al.sda.servermanagementsystem.requests.AuthenticationRequest;
import al.sda.servermanagementsystem.requests.AuthenticationResponse;
import al.sda.servermanagementsystem.requests.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        if (userRepository.findUserByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        userRepository.save(user);

        return AuthenticationResponse.builder()
                .confirmation("Registered")
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        var user = userRepository.findUserByUsername(authenticationRequest.getUsername())
                .orElseThrow(()-> new RuntimeException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        return AuthenticationResponse.builder()
                .confirmation("Logged in")
                .build();
    }
}
