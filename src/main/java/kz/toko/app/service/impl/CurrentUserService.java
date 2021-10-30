package kz.toko.app.service.impl;

import kz.toko.app.entity.UserEntity;
import kz.toko.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static kz.toko.app.util.AuthenticationUtils.getCurrentUsername;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public UserEntity getCurrentUser() {
        final var username = getCurrentUsername()
                .orElseThrow(() -> new AccessDeniedException("Unauthenticated user"));
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(format("User with username %s is not found", username)));
    }
}
