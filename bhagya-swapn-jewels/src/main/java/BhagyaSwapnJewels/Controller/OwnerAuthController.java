package BhagyaSwapnJewels.Controller;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/owner/auth")
public class OwnerAuthController {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public OwnerAuthController(
            @Qualifier("ownerAuthenticationManager") AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository) {

        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    // OWNER LOGIN
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

            boolean isOwner = authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority
                            -> authority.getAuthority().equals("ROLE_OWNER")
                    );

            if (!isOwner) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of(
                                "message",
                                "This account is not authorized for Owner login"
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
                    "message", "Owner login successful",
                    "username", authentication.getName(),
                    "role", "ROLE_OWNER",
                    "authenticated", true
            ));

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message",
                            "Invalid Owner username or password"
                    ));
        }
    }

    // CHECK CURRENT OWNER SESSION
    @GetMapping("/me")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> getCurrentOwner() {

        Authentication authentication
                = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(Map.of(
                "username", authentication.getName(),
                "role", "ROLE_OWNER",
                "authenticated", true
        ));
    }

    // OWNER LOGOUT
    @PostMapping("/logout")
    @PreAuthorize("hasRole('OWNER')")
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
                "message", "Owner logout successful",
                "authenticated", false
        ));
    }
}
