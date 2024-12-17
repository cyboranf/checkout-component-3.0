package com.component.checkout.infrastructure.adapters;

import com.component.checkout.service.TimeProvider;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class RealTimeProvider implements TimeProvider {

    private final Clock clock;

    public RealTimeProvider(Clock clock) {
        this.clock = clock;
    }

    @Override
    public LocalDateTime now() {
        return LocalDateTime.ofInstant(clock.instant(), clock.getZone());
    }

}
