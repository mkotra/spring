package pl.mkotra.spring.core.clock;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

@Component
public class StandardTimeSupplier implements Supplier<OffsetDateTime> {
    @Override
    public OffsetDateTime get() {
        return OffsetDateTime.now();
    }
}
