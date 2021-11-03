package kz.toko.app.config;

import kz.toko.app.filter.UserAutoSignUpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserAutoSignUpFilter userAutoSignUpFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors(configurer -> configurer.configurationSource(request -> corsConfiguration()))
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/users/**").authenticated()
                .antMatchers("/stores/**").authenticated()
                .antMatchers("/**").permitAll()
                .and()
                .oauth2ResourceServer()
                .jwt();

        http.addFilterAfter(userAutoSignUpFilter, BearerTokenAuthenticationFilter.class);
    }

    private CorsConfiguration corsConfiguration() {
        final var config = new CorsConfiguration().applyPermitDefaultValues();
        config.addAllowedMethod(HttpMethod.OPTIONS.name());
        config.addAllowedMethod(HttpMethod.DELETE.name());
        config.addAllowedMethod(HttpMethod.PATCH.name());
        return config;
    }
}
