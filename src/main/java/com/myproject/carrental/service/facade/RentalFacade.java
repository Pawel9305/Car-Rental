package com.myproject.carrental.service.facade;

import com.myproject.carrental.domain.RentalRequest;
import com.myproject.carrental.exception.CarNotFoundException;
import com.myproject.carrental.exception.RentalOverlappingException;
import com.myproject.carrental.exception.UserNotFoundException;
import com.myproject.carrental.service.RentalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalFacade {

    private final RentalService rentalService;

    public BigDecimal calculateRental(RentalRequest request) {
        log.info("Starting to process request");
        BigDecimal basicCost = rentalService.calculateBasicCost(request);
        log.info("Basic cost calculated");
        BigDecimal additionalCost = rentalService.additionalCosts(request);
        if (additionalCost.compareTo(BigDecimal.ZERO) != 0) {
            log.info("Additional cost calculated");
        } else {
            log.info("there were no additional costs");
        }
        return basicCost.add(additionalCost);
    }

    public void rentACar(RentalRequest request) throws UserNotFoundException, CarNotFoundException, RentalOverlappingException {
        log.info("Executing rent method");
        rentalService.rent(request);
    }
}
