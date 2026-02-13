package info.jab.ms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MyEndpointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getMyEndpoint_shouldReturnHelloWorld() throws Exception {
        mockMvc.perform(get("/api/v1/my-endpoint"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello World"));
    }
}
