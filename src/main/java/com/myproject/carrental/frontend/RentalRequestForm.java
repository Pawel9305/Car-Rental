package com.myproject.carrental.frontend;

import com.myproject.carrental.client.ExchangeRatesClient;
import com.myproject.carrental.domain.AdditionalEquipmentDto;
import com.myproject.carrental.domain.CarDto;
import com.myproject.carrental.service.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RentalRequestForm extends FormLayout {
    private RentalService rentalService;
    private RentalCalculator calculator;
    private CarService carService;
    private EquipmentService equipmentService;

    private MainView mainView;
    private ComboBox<City> location = new ComboBox<>("Location");
    private DatePicker from = new DatePicker("From");
    private DatePicker to = new DatePicker("To");
    private Button checkAvailability = new Button("Check");
    private Button send = new Button("Send");
    private CheckboxGroup<AdditionalEquipmentDto> additionalEquipmentCheckBox = new CheckboxGroup<>("Additional equipment");

    private Grid<CarDto> grid = new Grid<>(CarDto.class);

    public RentalRequestForm(MainView mainView, RentalService rentalService, CarService carService,
                             RentalCalculator calculator, EquipmentService equipmentService) {
        this.rentalService = rentalService;
        this.mainView = mainView;
        this.carService = carService;
        this.calculator = calculator;
        this.equipmentService = equipmentService;


        grid.setColumns("id", "brand", "model", "type", "price", "tankCapacity", "location");
        grid.setMaxHeight("300px");
        grid.setMaxWidth("1000px");
        location.setItems(City.values());
        checkAvailability.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        checkAvailability.setMaxWidth("100px");
        checkAvailability.addClickListener(e -> findCars());
        HorizontalLayout dates = new HorizontalLayout(from, to, location);
        dates.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        HorizontalLayout checkButton = new HorizontalLayout(checkAvailability);
        checkButton.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        VerticalLayout formLayout = new VerticalLayout(dates, checkButton);
        formLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        formLayout.setPadding(false);
        formLayout.setSpacing(true);

        grid.asSingleSelect().addValueChangeListener(event -> {
            CarDto selectedCar = event.getValue();
            if (selectedCar != null) {
                showRentalCostForm(selectedCar, calculateCost());
            }
        });

        grid.setVisible(false);
        add(formLayout, grid);
    }

    public void findCars() {
        LocalDate rentFrom = from.getValue();
        LocalDate rentTo = to.getValue();
        String chosenLocation = location.getValue().toString().toLowerCase();
        if (rentFrom != null && rentTo != null) {
            if (rentFrom.isAfter(LocalDate.now()) && rentTo.isAfter(rentFrom)) {
                List<CarDto> carsFound = rentalService.carsAvailableInAGivenPeriod(rentFrom, rentTo, chosenLocation);
                if (!carsFound.isEmpty()) {
                    grid.setItems(carsFound);
                    grid.setVisible(true);
                    mainView.updateFilteredCars(carsFound);
                    calculateCost();
                } else {
                    grid.setVisible(false);
                    mainView.updateFilteredCars(Collections.emptyList());
                    Notification.show("There is no available car for the provided period!", 3000, Notification.Position.TOP_CENTER);
                }
            } else {
                grid.setVisible(false);
                mainView.updateFilteredCars(Collections.emptyList());
                Notification.show("Invalid date range! Please select a valid range.", 3000, Notification.Position.TOP_CENTER);
            }
        } else {
            grid.setVisible(false);
            mainView.updateFilteredCars(Collections.emptyList());
            Notification.show("Please choose a date range.", 3000, Notification.Position.TOP_CENTER);
        }
    }

    public BigDecimal calculateCost() {
        LocalDate rentFrom = from.getValue();
        LocalDate rentTo = to.getValue();
        GridSingleSelectionModel<CarDto> selectionModel = (GridSingleSelectionModel<CarDto>) grid.getSelectionModel();
        Optional<CarDto> selectedCar = selectionModel.getSelectedItem();
        if (selectedCar.isPresent()) {
            return calculator.calculate(selectedCar.get().getId(), rentFrom, rentTo);
        }
        return BigDecimal.ZERO;
    }

    public void showRentalCostForm(CarDto selectedCar, BigDecimal cost) {
        RentalCostForm rentalCostForm = new RentalCostForm(selectedCar, cost, equipmentService);
        add(rentalCostForm);
    }
}

