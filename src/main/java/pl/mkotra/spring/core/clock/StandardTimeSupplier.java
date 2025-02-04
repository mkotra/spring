package pl.mkotra.spring.core.clock;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;
import java.util.function.Supplier;

@Component
public class StandardTimeSupplier implements Supplier<OffsetDateTime> {

    private static final ZoneId UTC_ZONE_ID = TimeZone.getTimeZone("UTC").toZoneId();

    @Override
    public OffsetDateTime get() {
        return OffsetDateTime.now(UTC_ZONE_ID).truncatedTo(ChronoUnit.MILLIS);
    }
}
