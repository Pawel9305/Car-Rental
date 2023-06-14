package com.myproject.carrental.frontend;

import com.myproject.carrental.client.ExchangeRatesClient;
import com.myproject.carrental.domain.CarDto;
import com.myproject.carrental.service.*;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route
@PageTitle("Main page | Car Rental")
public class MainView extends VerticalLayout {

    private CarService carService;

    private UserService userService;
    private RentalService rentalService;
    private RentalCalculator calculator;
    private EquipmentService equipmentService;
    private ExchangeRatesService exchangeRatesService;
    private ExchangeRatesClient exchangeRatesClient;

    private Grid<CarDto> grid = new Grid<>(CarDto.class);
    private UserRegisterForm registerUser;
    private CarSearchForm carSearchForm;
    private RentalRequestForm requestForm;


    public MainView(CarService carService, UserService userService, RentalService rentalService,
                    RentalCalculator calculator, EquipmentService equipmentService,
                    ExchangeRatesService exchangeRatesService, ExchangeRatesClient exchangeRatesClient) {
        this.carService = carService;
        this.userService = userService;
        this.rentalService = rentalService;
        this.calculator = calculator;
        this.equipmentService = equipmentService;
        this.exchangeRatesService = exchangeRatesService;
        this.exchangeRatesClient = exchangeRatesClient;

        carSearchForm = new CarSearchForm(this, carService);
        registerUser = new UserRegisterForm(this, userService);
        requestForm = new RentalRequestForm(this, rentalService, carService, calculator, equipmentService,
                exchangeRatesService, exchangeRatesClient);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        H1 header = new H1("CAR-RENTAL");
        add(header);
        grid.setColumns("brand", "model", "type", "tankCapacity", "price", "location");
        refreshGrid();

        Tab register = new Tab("Register");
        Tab showCars = new Tab("Our fleet");
        Tab rentACar = new Tab("Rent");
        Tab returnACar = new Tab("Return");
        String eventType = "click";
        register.getElement().addEventListener(eventType, e -> {
            hideGrid();
            showRegisterUser();
            hideCarSearchForm();
            requestForm.setVisible(false);
        });
        showCars.getElement().addEventListener(eventType, e -> {
            hideRegister();
            showGrid();
            showCarSearchForm();
            requestForm.setVisible(false);
        });
        rentACar.getElement().addEventListener(eventType, e -> {
            hideRegister();
            hideGrid();
            hideCarSearchForm();
            requestForm.setVisible(true);
        });
        returnACar.getElement().addEventListener(eventType, e -> {
            hideGrid();
            hideCarSearchForm();
            hideRegister();
            requestForm.setVisible(false);
        });
        Tabs tabs = new Tabs(register, showCars, rentACar, returnACar);
        requestForm.setMaxWidth("600px");
        registerUser.setMaxWidth("600px");

        Div registerUserContainer = new Div(registerUser);
        Div requestFormContainer = new Div(requestForm);
        add(tabs, carSearchForm, grid, registerUserContainer, requestFormContainer);

        requestForm.setVisible(false);
        hideGrid();
        hideRegister();
    }

    public void showGrid() {
        grid.setVisible(true);
    }

    public void hideGrid() {
        grid.setVisible(false);
    }

    public void refreshGrid() {
        grid.setItems(carService.getAllCars());
    }

    public void hideRegister() {
        registerUser.setVisible(false);
    }

    public void showRegisterUser() {
        registerUser.setVisible(true);
    }

    public void showCarSearchForm() {
        carSearchForm.setVisible(true);
    }

    public void hideCarSearchForm() {
        carSearchForm.setVisible(false);
    }

    public void updateFilteredCars(List<CarDto> filteredCars) {
        grid.setItems(filteredCars);
    }
}
