package com.rushi.jobportal.controller;

import com.rushi.jobportal.model.Role;
import com.rushi.jobportal.model.User;
import com.rushi.jobportal.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String email = body.get("email");
        String password = body.get("password");
        Role role;
        try {
            role = Role.valueOf(body.getOrDefault("role", "JOB_SEEKER").toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid role.");
        }

        User user = userService.register(name, email, password, role);
        return Map.of("message", "Registration successful.", "user", userView(user));
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body, HttpSession session) {
        User user = userService.login(body.get("email"), body.get("password"));
        session.setAttribute("USER_ID", user.getId());
        return Map.of("message", "Login successful.", "user", userView(user));
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpSession session) {
        session.invalidate();
        return Map.of("message", "Logged out successfully.");
    }

    @GetMapping("/me")
    public Map<String, Object> me(HttpSession session) {
        User user = currentUser(session);
        return Map.of("user", userView(user));
    }

    private User currentUser(HttpSession session) {
        Object id = session.getAttribute("USER_ID");
        if (id == null) throw new IllegalArgumentException("Not logged in.");
        return userServiceById(Long.valueOf(id));
    }

    private User userServiceById(Long id) {
        try {
            var field = UserService.class.getDeclaredField("userRepository");
            field.setAccessible(true);
            var repo = (com.rushi.jobportal.repository.UserRepository) field.get(userService);
            return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found."));
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private Map<String, Object> userView(User user) {
        return Map.of("id", user.getId(), "name", user.getName(), "email", user.getEmail(), "role", user.getRole());
    }
}
