package com.component.checkout.infrastructure.adapters;

import com.component.checkout.service.TimeProvider;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * A real-time provider implementation that returns the current time formatted for Poland.
 */
@Component
public class RealTimeProvider implements TimeProvider {

    private final Clock clock;
    private static final DateTimeFormatter POLISH_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public RealTimeProvider(Clock clock) {
        this.clock = clock;
    }

    /**
     * Returns the current date/time as a string formatted in Polish style: dd.MM.yyyy HH:mm:ss.
     *
     * @return Formatted current date/time string.
     */
    @Override
    public String nowDate() {
        LocalDateTime now = LocalDateTime.now(clock.withZone(ZoneId.of("Europe/Warsaw")));
        return now.format(POLISH_DATE_FORMAT);
    }

    /**
     * Returns the current date/time as a Date object.
     * Note: This implementation currently returns null.
     *
     * @return The current date as a Date object
     */
    @Override
    public Date now() {
        return Date.from(clock.instant());
    }
}
