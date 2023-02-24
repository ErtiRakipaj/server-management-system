package al.sda.servermanagementsystem.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class BaseController {
    private UsernamePasswordAuthenticationToken getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        return (UsernamePasswordAuthenticationToken) authentication;
    }

    protected String extractUsername() {
        if (Objects.isNull(getPrincipal())) {
            return "";
        }

        return getPrincipal().getName();
    }
}
