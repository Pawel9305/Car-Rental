package com.myproject.carrental.service;

import com.myproject.carrental.client.ExchangeRatesClient;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRatesService {

    private ExchangeRatesClient exchangeRatesClient;

    public BigDecimal getAmountToPay(final String from, final String to, final BigDecimal amount) {
        return exchangeRatesClient.calculateAmountToPay(from, to, amount);
    }
}
