package com.challenge.cache.controllers;

import com.challenge.cache.dto.CalculationRequest;
import com.challenge.cache.dto.CalculationResponse;
import com.challenge.cache.model.RequestHistory;
import com.challenge.cache.services.CalculationService;
import com.challenge.cache.services.HistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PercentageController.class)
class PercentageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculationService calculationService;

    @MockBean
    private HistoryService historyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCalculate_ReturnsCalculationResponse() throws Exception {
        CalculationRequest request = new CalculationRequest();
        request.setNum1(10.0);
        request.setNum2(5.0);

        CalculationResponse expectedResponse = new CalculationResponse();
        expectedResponse.setResult(16.5);

        Mockito.when(calculationService.calculateWithPercentage(10.0, 5.0))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/api/calculate")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value(16.5));

        // se verifica que también se haya llamado a logRequest
        Mockito.verify(historyService).logRequest(
            eq("/api/calculate"), any(String.class), any(String.class), eq(null)
        );
    }

    @Test
    void testGetHistory_ReturnsPaginatedHistory() throws Exception {
        RequestHistory history = new RequestHistory();
        history.setId(1L);
        history.setEndpoint("/api/calculate");
        history.setParameters("num1=10.0&num2=5.0");
        history.setResponse("16.5");
        history.setError(null);
        history.setTimestamp(LocalDateTime.now());

        Page<RequestHistory> page = new PageImpl<>(List.of(history), PageRequest.of(0, 10), 1);

        Mockito.when(historyService.getAllHistory(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/history")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(1)))
            .andExpect(jsonPath("$.content[0].endpoint").value("/api/calculate"))
            .andExpect(jsonPath("$.content[0].parameters").value("num1=10.0&num2=5.0"))
            .andExpect(jsonPath("$.content[0].response").value("16.5"));
    }

    @Test
    void testGetHistory_WhenNoData_ReturnsEmptyPage() throws Exception {
        Page<RequestHistory> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);

        Mockito.when(historyService.getAllHistory(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/history")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(0)))
            .andExpect(jsonPath("$.totalElements").value(0));
    }
}