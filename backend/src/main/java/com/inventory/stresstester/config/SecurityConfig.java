package com.inventory.stresstester.config;

import com.inventory.stresstester.repository.AppUserRepository;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean SecretKey jwtKey(@Value("${auth.jwt.secret-base64}") String encoded) {
        byte[] bytes = Base64.getDecoder().decode(encoded);
        if (bytes.length < 32) throw new IllegalArgumentException("JWT secret must decode to at least 32 bytes");
        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    @Bean JwtEncoder jwtEncoder(SecretKey key) {
        return new NimbusJwtEncoder(new ImmutableSecret<SecurityContext>(key));
    }

    @Bean JwtDecoder jwtDecoder(SecretKey key) {
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }

    @Bean CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http, AppUserRepository users) throws Exception {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        // Resolve current role from database, not from a potentially stale role claim in the token.
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var user = users.findByEmailIgnoreCase(jwt.getSubject())
                    .orElseThrow(() -> new BadCredentialsException("Account no longer exists"));
            if (user.getRole() == null) return List.of();
            return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()));
        });

        return http.csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a -> a
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/health", "/api/auth/login", "/api/auth/register").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/roles/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/users/**").denyAll()
                .requestMatchers(HttpMethod.PUT, "/api/users/**").denyAll()
                .requestMatchers(HttpMethod.PATCH, "/api/users/**").denyAll()
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").denyAll()
                .requestMatchers("/api/users/**").hasRole("ADMIN")
                // Analysts can execute simulations and calculations, but not edit operational master data.
                .requestMatchers("/api/simulation-engine/**", "/api/calculations/**", "/api/analysis/**")
                    .hasAnyRole("ADMIN", "INVENTORY_MANAGER", "ANALYST")
                // Inventory managers and admins can modify operational records.
                .requestMatchers(HttpMethod.POST, "/api/**").hasAnyRole("ADMIN", "INVENTORY_MANAGER")
                .requestMatchers(HttpMethod.PUT, "/api/**").hasAnyRole("ADMIN", "INVENTORY_MANAGER")
                .requestMatchers(HttpMethod.PATCH, "/api/**").hasAnyRole("ADMIN", "INVENTORY_MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/**").hasAnyRole("ADMIN", "INVENTORY_MANAGER")
                // Analysts may view records used by their simulations.
                .requestMatchers(HttpMethod.GET, "/api/**")
                    .hasAnyRole("ADMIN", "INVENTORY_MANAGER", "ANALYST")
                .requestMatchers("/api/**").denyAll()
                .anyRequest().denyAll())
            .oauth2ResourceServer(o -> o.jwt(j -> j.jwtAuthenticationConverter(converter)))
            .build();
    }
}
