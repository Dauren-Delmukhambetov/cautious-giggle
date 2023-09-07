package kz.toko.app.filter;

import kz.toko.api.model.CreateUserRequest;
import kz.toko.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAutoSignUpFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("Starting UserAutoSignUpFilter processing");

        final var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (nonNull(authentication) && authentication.getPrincipal() instanceof Jwt) {
            log.debug("Current user's ID is {}", authentication.getName());
            final var jwt = (Jwt) authentication.getPrincipal();

            if (!containsMandatoryClaims(jwt)) {
                response.sendError(BAD_REQUEST.value(),
                        format("The JWT token must contain %s claims. Available claims: %s", mandatoryClaims, jwt.getClaims().keySet()));
                return;
            }

            log.debug("Current user's email is {}", jwt.getClaimAsString("email"));

            final var username = jwt.getSubject();

            try {
                userService.loadUserByUsername(username);
            } catch (UsernameNotFoundException ex) {
                final var createUserRequest = buildCreateUserRequest(jwt);
                final var createdUser = userService.save(createUserRequest);
                log.info("New user with email {} has been automatically created", createdUser.getEmail());
            }
        }

        log.debug("Completing UserAutoSignUpFilter processing");

        filterChain.doFilter(request, response);
    }

    private boolean containsMandatoryClaims(Jwt jwt) {
        return jwt.getClaims().keySet().containsAll(mandatoryClaims);
    }

    private CreateUserRequest buildCreateUserRequest(Jwt jwt) {
        return new CreateUserRequest()
                .username(jwt.getSubject())
                .firstName(jwt.getClaimAsString("given_name"))
                .lastName(jwt.getClaimAsString("family_name"))
                .email(jwt.getClaimAsString("email"));
    }

    private final Set<String> mandatoryClaims = Set.of("given_name", "family_name", "email");
}
