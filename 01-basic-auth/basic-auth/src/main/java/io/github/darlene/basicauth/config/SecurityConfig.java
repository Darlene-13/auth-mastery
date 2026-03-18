package io.github.darlene.basicauth.config;

import io.github.darlene.basicauth.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HTTPSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 *  NOTES
 * @Configuration annotation tells spring that this class contains bean definitions that spring will scan it on startup.
 * @EnableWebSecurity activates spring security for the whole application without this nothing is secured.
 * SecurityFilterchain is the most important method and it is akin to a rulebook for all incoming requests
 * csrf(csrf -> csrf.disable()) (CROSS SITE REQUEST FORGERY) protection is meant for browser-based apps with cookies and sessions. Rest apis are stateless, no cookies, no sessions thus csrf does not have to apply and should be disable so as not tto break the api.
 * authorizehttp requests is where we define who can access what while requestMatchers("h2-console/)").permitAll() means the console is public thus no authentication to access it, while anyRequest().authenticated() means that any other request should be authenticated.
 * BCrypt is the industrial standarda for password hashing and is irreversable it automatically adds salts so two similar passwords are hashed differently... but is it a bit slow...
 */
/
@Configuration
@EnableWebSecurity
@RequiredArgsConstrcutor
public SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .crsf(crsf -> crsf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.realmName("BasicAuth App"))
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                );
        return http.build();
    }

    /**
     * This is the bridge between spring security and the database.
     * DaoAuthenticationProvider is springs built in provider for db-backed auth that need userdetailsservice and password encoder
     */
    /
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * This exposed springs authentication manager as a bean
     */

    @Bean
    public  AuthenticationManager authenticationManager( AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}