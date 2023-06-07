package com.myproject.carrental.frontend;

import com.myproject.carrental.service.RentalService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;

public class RentalRequestForm extends FormLayout {

    private Button send = new Button("Send");
    private MainView mainView;
    private RentalService rentalService;

    public RentalRequestForm(MainView mainView, RentalService rentalService) {
        this.rentalService = rentalService;
        this.mainView = mainView;
    }
}
