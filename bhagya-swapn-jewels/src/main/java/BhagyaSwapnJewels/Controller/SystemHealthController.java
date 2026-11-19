package BhagyaSwapnJewels.Controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import BhagyaSwapnJewels.service.SystemHealthService;

@RestController
@RequestMapping("/api/manager/system")
public class SystemHealthController {

    private final SystemHealthService systemHealthService;

    public SystemHealthController(
            SystemHealthService systemHealthService) {
        this.systemHealthService = systemHealthService;
    }

    @GetMapping("/health")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Map<String, Object>> getSystemHealth() {

        Map<String, Object> health
                = systemHealthService.getSystemHealth();

        return ResponseEntity.ok(health);
    }
}
