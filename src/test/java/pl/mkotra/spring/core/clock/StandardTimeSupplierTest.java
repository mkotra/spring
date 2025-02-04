package pl.mkotra.spring.core.clock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class StandardTimeSupplierTest {

    StandardTimeSupplier standardTimeSupplier = new StandardTimeSupplier();

    @Test
    @DisplayName("get returns result with valid offset")
    void get() {
        //when
        OffsetDateTime result = standardTimeSupplier.get();

        //then
        assertThat(result.getOffset()).hasToString("Z");
    }
}