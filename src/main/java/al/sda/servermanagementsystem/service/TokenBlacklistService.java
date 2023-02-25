package al.sda.servermanagementsystem.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistService {
    private final Set<String> blacklist = Collections.synchronizedSet(new HashSet<>());

    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }

    public void blacklistToken(String token) {
        blacklist.add(token);
    }

    public void unblacklistToken(String token) {
        blacklist.remove(token);
    }
}
