package info.jab.ms.controller;

import org.junit.jupiter.api.DisplayName;
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
class MyCalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Divide should return success response when parameters are valid")
    void divide_shouldReturnSuccess_whenParametersAreValid() throws Exception {
        mockMvc.perform(get("/api/v1/calculator/divide")
                        .param("a", "10")
                        .param("b", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(5.0));
    }

    @Test
    @DisplayName("Divide should return success response with decimal result")
    void divide_shouldReturnSuccess_whenResultIsDecimal() throws Exception {
        mockMvc.perform(get("/api/v1/calculator/divide")
                        .param("a", "7")
                        .param("b", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(3.5));
    }

    @Test
    @DisplayName("Divide should return error when parameter 'a' is negative")
    void divide_shouldReturnError_whenParameterAIsNegative() throws Exception {
        mockMvc.perform(get("/api/v1/calculator/divide")
                        .param("a", "-10")
                        .param("b", "2"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.problem.detail").value("Parameter 'a' must be non-negative"))
                .andExpect(jsonPath("$.problem.status").value(500));
    }

    @Test
    @DisplayName("Divide should return error when parameter 'b' is negative")
    void divide_shouldReturnError_whenParameterBIsNegative() throws Exception {
        mockMvc.perform(get("/api/v1/calculator/divide")
                        .param("a", "10")
                        .param("b", "-2"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.problem.detail").value("Parameter 'b' must be non-negative"))
                .andExpect(jsonPath("$.problem.status").value(500));
    }

    @Test
    @DisplayName("Divide should return error when both parameters are negative")
    void divide_shouldReturnError_whenBothParametersAreNegative() throws Exception {
        mockMvc.perform(get("/api/v1/calculator/divide")
                        .param("a", "-10")
                        .param("b", "-2"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.problem.detail").value("Parameter 'a' must be non-negative"))
                .andExpect(jsonPath("$.problem.status").value(500));
    }

    @Test
    @DisplayName("Divide should return error when dividing by zero")
    void divide_shouldReturnError_whenDividingByZero() throws Exception {
        mockMvc.perform(get("/api/v1/calculator/divide")
                        .param("a", "10")
                        .param("b", "0"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.problem.detail").value("Division by zero is not allowed"))
                .andExpect(jsonPath("$.problem.status").value(500));
    }

    @Test
    @DisplayName("Divide should return success when dividing zero by a positive number")
    void divide_shouldReturnSuccess_whenDividingZeroByPositive() throws Exception {
        mockMvc.perform(get("/api/v1/calculator/divide")
                        .param("a", "0")
                        .param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(0.0));
    }
}
