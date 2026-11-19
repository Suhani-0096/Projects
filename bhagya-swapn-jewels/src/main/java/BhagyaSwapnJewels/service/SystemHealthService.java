package BhagyaSwapnJewels.service;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;
import java.sql.Connection;

import org.springframework.stereotype.Service;

@Service
public class SystemHealthService {

    private final DataSource dataSource;

    public SystemHealthService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, Object> getSystemHealth() {

        Map<String, Object> health = new LinkedHashMap<>();

        health.put("backend", "UP");

        try (Connection connection = dataSource.getConnection()) {

            boolean databaseConnected
                    = connection != null && connection.isValid(3);

            health.put(
                    "database",
                    databaseConnected ? "UP" : "DOWN"
            );

        } catch (Exception exception) {

            health.put("database", "DOWN");
        }

        health.put(
                "overall",
                "UP".equals(health.get("backend"))
                && "UP".equals(health.get("database"))
                ? "UP" : "DEGRADED"
        );

        return health;
    }
}
