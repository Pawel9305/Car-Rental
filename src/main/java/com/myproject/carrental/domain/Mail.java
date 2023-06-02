package com.myproject.carrental.domain;


import lombok.*;

@Getter
public class Mail {
    private final String mailTo;
    private String subject;
    private String message;

    private long carId;

    private long rentalId;

    public Mail(String mailTo) {
        this.mailTo = mailTo;
    }

    public Mail setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public Mail setMessage(String message) {
        this.message = message;
        return this;
    }

    public Mail setCarId(long carId) {
        this.carId = carId;
        return this;
    }

    public Mail setRentalId(long rentalId) {
        this.rentalId = rentalId;
        return this;
    }

    public String getMessage() {
        return "Car ID:" + carId + "\nRental ID: " + rentalId + "\n" + message;
    }
}
