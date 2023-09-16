package kz.toko.app.config;

import kz.toko.app.filter.UserAutoSignUpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserAutoSignUpFilter userAutoSignUpFilter;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(configurer -> configurer.configurationSource(request -> corsConfiguration()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        (authz) -> authz.requestMatchers("/users/**").authenticated()
                                .requestMatchers("/stores/**").authenticated()
                                .requestMatchers("/store-items/**").authenticated()
                                .requestMatchers("/**").permitAll()
                )
                .oauth2ResourceServer(
                        (oauth2ResourceServer) -> oauth2ResourceServer.jwt(
                                (jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
                        )
                );

        http.addFilterAfter(userAutoSignUpFilter, BearerTokenAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation("https://accounts.google.com");
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        final var config = new CorsConfiguration().applyPermitDefaultValues();
        config.addAllowedMethod(HttpMethod.OPTIONS.name());
        config.addAllowedMethod(HttpMethod.DELETE.name());
        config.addAllowedMethod(HttpMethod.PATCH.name());
        config.addAllowedMethod(HttpMethod.PUT.name());
        return config;
    }
}
