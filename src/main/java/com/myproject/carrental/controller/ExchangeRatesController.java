package com.myproject.carrental.controller;


import com.myproject.carrental.service.ExchangeRatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@RestController
@CrossOrigin("*")
@RequestMapping("v1/exchange")
@RequiredArgsConstructor
public class ExchangeRatesController {

    private final ExchangeRatesService exchangeRatesService;

    @GetMapping(value = "convert")
    public ResponseEntity<BigDecimal> getAmountToPay(@RequestParam("from") @NotNull final String from,
                                                     @RequestParam("to") @NotNull final String to,
                                                     @RequestParam("amount") @NotNull final BigDecimal amount) {
        return ResponseEntity.ok(exchangeRatesService.getAmountToPay(from, to, amount));
    }
}

