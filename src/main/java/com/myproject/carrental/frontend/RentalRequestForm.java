package com.myproject.carrental.frontend;

import com.myproject.carrental.client.ExchangeRatesClient;
import com.myproject.carrental.domain.CarDto;
import com.myproject.carrental.domain.RentalRequest;
import com.myproject.carrental.exception.CarNotFoundException;
import com.myproject.carrental.exception.RentalOverlappingException;
import com.myproject.carrental.exception.UserNotFoundException;
import com.myproject.carrental.service.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RentalRequestForm extends FormLayout {
    private RentalService rentalService;
    private RentalCalculator calculator;
    private CarService carService;
    private EquipmentService equipmentService;
    private ExchangeRatesService exchangeRatesService;
    private ExchangeRatesClient exchangeRatesClient;
    private MainView mainView;
    private RentalForm currentRentalForm;
    private ComboBox<City> location = new ComboBox<>("Location");
    private DatePicker from = new DatePicker("From");
    private DatePicker to = new DatePicker("To");
    private Button checkAvailability = new Button("Check");
    private Button send = new Button("Send");
    private Button rent = new Button("Rent");
    private Grid<CarDto> carDtoGrid = new Grid<>(CarDto.class);

    public RentalRequestForm(MainView mainView, RentalService rentalService, CarService carService,
                             RentalCalculator calculator, EquipmentService equipmentService,
                             ExchangeRatesService exchangeRatesService, ExchangeRatesClient exchangeRatesClient) {
        this.rentalService = rentalService;
        this.mainView = mainView;
        this.carService = carService;
        this.calculator = calculator;
        this.equipmentService = equipmentService;
        this.exchangeRatesService = exchangeRatesService;
        this.exchangeRatesClient = exchangeRatesClient;

        rent.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        rent.setVisible(false);
        rent.addClickListener(e -> {
            try {
                rentACar();
            } catch (UserNotFoundException ex) {
                Notification.show("User not found!", 3000, Notification.Position.TOP_CENTER);
            } catch (RentalOverlappingException ex) {
                Notification.show("There was a problem with availability of a chosen car", 3000, Notification.Position.TOP_CENTER);
            } catch (CarNotFoundException ex) {
                Notification.show("Car not found!", 3000, Notification.Position.TOP_CENTER);
            }
        });
        carDtoGrid.setColumns("id", "brand", "model", "type", "price", "tankCapacity", "location");
        carDtoGrid.setMaxHeight("300px");
        carDtoGrid.setMaxWidth("1000px");
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

        carDtoGrid.asSingleSelect().addValueChangeListener(event -> {
            CarDto selectedCar = event.getValue();
            if (selectedCar != null) {
                showRentalCostForm(selectedCar, calculateCost());
                add(rent);
                rent.setVisible(true);
            }
        });

        carDtoGrid.setVisible(false);
        add(formLayout, carDtoGrid);
    }

    public void findCars() {
        LocalDate rentFrom = from.getValue();
        LocalDate rentTo = to.getValue();
        String chosenLocation = location.getValue().toString().toLowerCase();
        if (rentFrom != null && rentTo != null) {
            if (rentFrom.isAfter(LocalDate.now()) && rentTo.isAfter(rentFrom)) {
                List<CarDto> carsFound = rentalService.carsAvailableInAGivenPeriod(rentFrom, rentTo, chosenLocation);
                if (!carsFound.isEmpty()) {
                    carDtoGrid.setItems(carsFound);
                    carDtoGrid.setVisible(true);
                    mainView.updateFilteredCars(carsFound);
                    calculateCost();
                } else {
                    carDtoGrid.setVisible(false);
                    mainView.updateFilteredCars(Collections.emptyList());
                    Notification.show("There is no available car for the provided period!", 3000, Notification.Position.TOP_CENTER);
                }
            } else {
                carDtoGrid.setVisible(false);
                mainView.updateFilteredCars(Collections.emptyList());
                Notification.show("Invalid date range! Please select a valid range.", 3000, Notification.Position.TOP_CENTER);
            }
        } else {
            carDtoGrid.setVisible(false);
            mainView.updateFilteredCars(Collections.emptyList());
            Notification.show("Please choose a date range.", 3000, Notification.Position.TOP_CENTER);
        }
    }
//TODO add weather forecast
    public BigDecimal calculateCost() {
        LocalDate rentFrom = from.getValue();
        LocalDate rentTo = to.getValue();
        GridSingleSelectionModel<CarDto> selectionModel = (GridSingleSelectionModel<CarDto>) carDtoGrid.getSelectionModel();
        Optional<CarDto> selectedCar = selectionModel.getSelectedItem();
        return selectedCar
                .map(carDto -> calculator.calculate(carDto.getId(), rentFrom, rentTo).setScale(2, RoundingMode.HALF_UP))
                .orElse(BigDecimal.ZERO);
    }

    public void showRentalCostForm(CarDto selectedCar, BigDecimal cost) {
        if (currentRentalForm != null) {
            remove(currentRentalForm);
        }
        currentRentalForm = new RentalForm(selectedCar, cost, equipmentService, exchangeRatesService, exchangeRatesClient);
        add(currentRentalForm);
    }

    public void rentACar() throws UserNotFoundException, RentalOverlappingException, CarNotFoundException {
        LocalDate rentFrom = from.getValue();
        LocalDate rentTo = to.getValue();
        GridSingleSelectionModel<CarDto> selectionModel = (GridSingleSelectionModel<CarDto>) carDtoGrid.getSelectionModel();
        Optional<CarDto> selectedCar = selectionModel.getSelectedItem();
        if (selectedCar.isPresent()) {
            rentalService.rent(new RentalRequest(49L, selectedCar.get().getId(), rentFrom, rentTo,
                    location.getValue().toString(), currentRentalForm.getSelectedEquipmentIds()));
        }
    }
}

