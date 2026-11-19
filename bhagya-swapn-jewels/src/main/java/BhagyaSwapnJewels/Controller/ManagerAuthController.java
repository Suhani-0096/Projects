package BhagyaSwapnJewels.Controller;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manager/auth")
public class ManagerAuthController {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public ManagerAuthController(
            @Qualifier("managerAuthenticationManager") AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository) {

        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    // MANAGER LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> credentials,
            HttpServletRequest request,
            HttpServletResponse response) {

        String username = credentials.get("username");
        String password = credentials.get("password");

        if (username == null || password == null
                || username.isBlank() || password.isBlank()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Username and password are required"
                    ));
        }

        try {
            Authentication authentication
                    = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    username, password
                            )
                    );

            boolean isManager = authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority
                            -> authority.getAuthority().equals("ROLE_MANAGER")
                    );

            if (!isManager) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of(
                                "message",
                                "This account is not authorized for Manager login"
                        ));
            }

            request.getSession(true);
            request.changeSessionId();

            SecurityContext context
                    = SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            securityContextRepository.saveContext(
                    context, request, response
            );

            return ResponseEntity.ok(Map.of(
                    "message", "Website Manager login successful",
                    "username", authentication.getName(),
                    "role", "ROLE_MANAGER",
                    "authenticated", true
            ));

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message",
                            "Invalid Manager username or password"
                    ));
        }
    }

    // CHECK CURRENT MANAGER SESSION
    @GetMapping("/me")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> getCurrentManager() {

        Authentication authentication
                = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(Map.of(
                "username", authentication.getName(),
                "role", "ROLE_MANAGER",
                "authenticated", true
        ));
    }

    // MANAGER LOGOUT
    @PostMapping("/logout")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        SecurityContext context
                = SecurityContextHolder.createEmptyContext();

        SecurityContextHolder.clearContext();

        securityContextRepository.saveContext(
                context, request, response
        );

        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }

        return ResponseEntity.ok(Map.of(
                "message", "Manager logout successful",
                "authenticated", false
        ));
    }
}
