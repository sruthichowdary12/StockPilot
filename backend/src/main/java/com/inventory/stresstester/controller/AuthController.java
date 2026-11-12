package com.inventory.stresstester.controller;
import com.inventory.stresstester.dto.auth.AuthDtos.*;
import com.inventory.stresstester.entity.*;
import com.inventory.stresstester.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AppUserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;
    private final JwtEncoder jwtEncoder;
    public AuthController(AppUserRepository users, RoleRepository roles, PasswordEncoder encoder, JwtEncoder jwtEncoder) {
        this.users=users; this.roles=roles; this.encoder=encoder; this.jwtEncoder=jwtEncoder;
    }
    private UserResponse userResponse(AppUser user) {
        return new UserResponse(user.getId(),user.getName(),user.getEmail(),user.getRole().getRoleName());
    }
    private AuthResponse issue(AppUser user) {
        Instant now=Instant.now();
        JwtClaimsSet claims=JwtClaimsSet.builder().issuer("stockpilot")
            .issuedAt(now).expiresAt(now.plus(2,ChronoUnit.HOURS))
            .subject(user.getEmail()).claim("role",user.getRole().getRoleName()).build();
        JwsHeader header=JwsHeader.with(MacAlgorithm.HS256).build();
        String token=jwtEncoder.encode(JwtEncoderParameters.from(header,claims)).getTokenValue();
        return new AuthResponse(token,"Bearer",userResponse(user));
    }
    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED) @Transactional
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        String email=request.email().trim().toLowerCase(Locale.ROOT);
        if(users.existsByEmailIgnoreCase(email)) throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already registered");
        Role role=roles.findByRoleName("ANALYST").orElseGet(() -> {
            Role r=new Role();r.setRoleName("ANALYST");return roles.save(r);
        });
        AppUser user=new AppUser();user.setName(request.name().trim());user.setEmail(email);
        user.setPasswordHash(encoder.encode(request.password()));user.setRole(role);
        return issue(users.saveAndFlush(user));
    }
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        AppUser user=users.findByEmailIgnoreCase(request.email().trim())
            .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        if(!encoder.matches(request.password(),user.getPasswordHash()))
            throw new BadCredentialsException("Invalid email or password");
        return issue(user);
    }
    @GetMapping("/me")
    public UserResponse me(org.springframework.security.core.Authentication authentication) {
        AppUser user=users.findByEmailIgnoreCase(authentication.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        return userResponse(user);
    }
}
