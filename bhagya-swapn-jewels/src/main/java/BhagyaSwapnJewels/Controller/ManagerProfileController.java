package BhagyaSwapnJewels.Controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manager")
public class ManagerProfileController {

    @GetMapping("/profile")
    public ResponseEntity<?> getManagerProfile(
            Authentication authentication) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Manager profile retrieved successfully",
                        "username", authentication.getName(),
                        "role", "ROLE_MANAGER",
                        "access", "WEBSITE_MANAGEMENT"
                )
        );
    }
}
