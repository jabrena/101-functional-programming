package info.jab.ms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MyFaultyEndpointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getMyFaultyEndpoint_shouldReturnProblemDetail() throws Exception {
        mockMvc.perform(get("/api/v1/my-faulty-endpoint"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.problem.detail").value("My Faulty Service is not available"))
                .andExpect(jsonPath("$.problem.status").value(500));
    }
}
