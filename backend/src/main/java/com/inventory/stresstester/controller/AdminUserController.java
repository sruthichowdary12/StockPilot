package com.inventory.stresstester.controller;

import com.inventory.stresstester.entity.AppUser;
import com.inventory.stresstester.entity.Role;
import com.inventory.stresstester.repository.AppUserRepository;
import com.inventory.stresstester.repository.RoleRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private static final Set<String> ALLOWED_ROLES = Set.of("ADMIN", "INVENTORY_MANAGER", "ANALYST");
    private final AppUserRepository users;
    private final RoleRepository roles;

    public AdminUserController(AppUserRepository users, RoleRepository roles) {
        this.users = users;
        this.roles = roles;
    }

    public record UserSummary(Long id, String name, String email, String role) {}
    public record ChangeRoleRequest(@NotBlank String role) {}

    private UserSummary summary(AppUser user) {
        return new UserSummary(user.getId(), user.getName(), user.getEmail(),
                user.getRole() == null ? null : user.getRole().getRoleName());
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<UserSummary> list() {
        return users.findAll().stream()
                .sorted(Comparator.comparing(AppUser::getId))
                .map(this::summary).toList();
    }

    @PatchMapping("/{id}/role")
    @Transactional
    public UserSummary changeRole(@PathVariable Long id,
                                  @Valid @RequestBody ChangeRoleRequest request,
                                  Authentication authentication) {
        AppUser target = users.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        String requested = request.role().trim().toUpperCase(Locale.ROOT);
        if (!ALLOWED_ROLES.contains(requested)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role");
        }
        if (target.getEmail().equalsIgnoreCase(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot change your own role");
        }
        Role role = roles.findByRoleName(requested)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Role not found in database: " + requested));
        target.setRole(role);
        return summary(users.save(target));
    }
}
