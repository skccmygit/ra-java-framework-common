package kr.co.skcc.oss.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.availability.ReadinessStateHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomHealthIndicator implements HealthIndicator  {

    private Status status = Status.UP;

    @Autowired
    ReadinessStateHealthIndicator readinessStateHealthIndicator;

    @Override
    public Health health() {

        Health readinessHealth = readinessStateHealthIndicator.getHealth(false);
        // readiness status 와 custom status 확인 --> 하나라도 DOWN 이면 DOWN
        if (Status.DOWN == readinessHealth.getStatus() || Status.DOWN == this.status) {
            return Health.down().build();
        } else {
            return Health.up().build();
        }
    }

    public void setStatus(boolean status) {
        this.status =  status ? Status.UP : Status.DOWN;
    }
}
