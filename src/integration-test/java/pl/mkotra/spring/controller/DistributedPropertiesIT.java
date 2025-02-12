package pl.mkotra.spring.controller;

import org.junit.jupiter.api.Test;
import pl.mkotra.spring.BaseIT;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DistributedPropertiesIT extends BaseIT {

    @Test
    void getConfigFromValue() throws Exception {

        mockMvc.perform(get("/config-from-value"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("dummy"));
    }

    @Test
    void getConfigFromProperties() throws Exception {

        mockMvc.perform(get("/config-from-properties"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("dummy"));
    }
}
