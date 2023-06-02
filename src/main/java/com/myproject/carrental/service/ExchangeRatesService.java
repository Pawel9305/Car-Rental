package com.myproject.carrental.service;

import com.myproject.carrental.client.ExchangeRatesClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ExchangeRatesService {

    private final ExchangeRatesClient exchangeRatesClient;

    public BigDecimal getAmountToPay(final String from, final String to, final BigDecimal amount) {
        return exchangeRatesClient.calculateAmountToPay(from, to, amount);
    }
}
