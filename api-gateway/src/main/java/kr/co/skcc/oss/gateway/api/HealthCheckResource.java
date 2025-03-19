package kr.co.skcc.oss.gateway.api;

import kr.co.skcc.oss.gateway.config.CustomHealthIndicator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/gw/health")
@RequiredArgsConstructor
public class HealthCheckResource {

    @Autowired
    private CustomHealthIndicator customHealthIndicator;

    @GetMapping(path = "/check")
    public ResponseEntity<Health> getHealthCheck() {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> call health check");
        Health health = customHealthIndicator.getHealth(false);
        if (health.getStatus() != Status.UP) {
            return new ResponseEntity<>(health, HttpStatus.SERVICE_UNAVAILABLE);
        }

        return new ResponseEntity<>(health, HttpStatus.OK);
    }

    @GetMapping(path = "/down")
    public ResponseEntity<?> setReadinessDown() {
        log.info(">>>>>>>> call health down");
        customHealthIndicator.setStatus(false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
