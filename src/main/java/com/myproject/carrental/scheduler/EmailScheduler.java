package com.myproject.carrental.scheduler;

import com.myproject.carrental.domain.Mail;
import com.myproject.carrental.domain.Rental;
import com.myproject.carrental.repository.RentalRepository;
import com.myproject.carrental.service.SimpleEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class EmailScheduler {

    private final String SUBJECT = "Your rental car is due tomorrow!";
    private final RentalRepository rentalRepository;
    private final SimpleEmailService simpleEmailService;

    @Scheduled(cron = "0 0 * * * *")
    private void dailyScheduledRentalReminder() {
        List<Rental> emailsToSend = rentalRepository.findAll().stream()
                .filter(rental -> rental.getFrom().minusDays(1).isEqual(LocalDate.now()))
                .toList();

        for (Rental rental : emailsToSend) {
            Mail mail = new Mail.MailBuilder()
                    .setMailTo(rental.getUser().getEmail())
                    .setSubject(SUBJECT)
                    .setMessage(createRentalMessage(rental))
                    .setCarId(rental.getCar().getId())
                    .setRentalId(rental.getId())
                    .build();
            simpleEmailService.send(mail);
        }
    }

    private String createRentalMessage(Rental rental) {
        return "Hello, " + rental.getUser().getName()
                + "\nRental details: " + rental.getCar().getBrand() + "\s" + rental.getCar().getModel()
                + "\nfor a time period of: " + rental.getFrom() + " - " + rental.getTo() + "\nTotal cost: "
                + rental.getTotalCost() + "\nLocation: " + rental.getLocation();
    }
}
