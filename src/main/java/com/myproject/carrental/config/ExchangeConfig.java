package com.myproject.carrental.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ExchangeConfig {

    @Value("${exchange.api.convert.url}")
    private String exchangeUrl;

}
