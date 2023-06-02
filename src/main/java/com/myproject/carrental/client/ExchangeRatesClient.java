package com.myproject.carrental.client;

import com.myproject.carrental.domain.exchangerates.ExchangeRateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExchangeRatesClient {

    @Value("${exchange.api.convert.url}")
    private String exchangeApi;

    private final RestTemplate restTemplate;

    public BigDecimal calculateAmountToPay(final String from, final String to, final BigDecimal amount) {
        URI url = UriComponentsBuilder.fromHttpUrl(exchangeApi)
                .queryParam("from", from)
                .queryParam("to", to)
                .queryParam("amount", amount)
                .build()
                .encode()
                .toUri();

        ExchangeRateDto response = restTemplate.getForObject(url, ExchangeRateDto.class);

        return Optional.ofNullable(response).orElse(new ExchangeRateDto()).getResult();
    }
}
