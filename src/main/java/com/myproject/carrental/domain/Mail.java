package com.myproject.carrental.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Mail {
    private final String mailTo;
    private String subject;
    private String message;
    private long carId;
    private long rentalId;

    public static class MailBuilder {

        private String mailTo;
        private String subject;
        private String message;
        private long carId;
        private long rentalId;

        public MailBuilder setMailTo(String mailTo) {
            this.mailTo = mailTo;
            return this;
        }

        public MailBuilder setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public MailBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public MailBuilder setCarId(long carId) {
            this.carId = carId;
            return this;
        }

        public MailBuilder setRentalId(long rentalId) {
            this.rentalId = rentalId;
            return this;
        }

        public Mail build() {
            return new Mail(mailTo, subject, message, carId, rentalId);
        }
    }

    public String getMessage() {
        return "Car ID:" + carId + "\nRental ID: " + rentalId + "\n" + message;
    }
}
