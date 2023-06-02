package com.myproject.carrental.controller;


import com.myproject.carrental.service.ExchangeRatesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(ExchangeRatesController.class)
class ExchangeRatesControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRatesService exchangeRatesService;

    @Test
    void shouldReturnAProperValue() throws Exception {
        //Given
        when(exchangeRatesService.getAmountToPay("EUR", "PLN", new BigDecimal("200"))).thenReturn(new BigDecimal("910"));

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/exchange/convert?from=EUR&to=PLN&amount=200"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("910"));
    }
}
