package com.myproject.carrental.frontend;

import com.myproject.carrental.domain.CarDto;
import com.myproject.carrental.service.CarService;
import com.myproject.carrental.service.UserService;
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

    private Grid<CarDto> grid = new Grid<>(CarDto.class);
    private UserRegisterForm registerUser;
    private CarSearchForm carSearchForm;

    public MainView(CarService carService, UserService userService) {
        this.carService = carService;
        this.userService = userService;
        carSearchForm = new CarSearchForm(this, carService);
        registerUser = new UserRegisterForm(this, userService);
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
        });
        showCars.getElement().addEventListener(eventType, e -> {
            hideRegister();
            showGrid();
            showCarSearchForm();
        });
        rentACar.getElement().addEventListener(eventType, e -> {
            hideRegister();
            hideGrid();
            hideCarSearchForm();
        });
        returnACar.getElement().addEventListener(eventType, e -> {
            hideGrid();
            hideCarSearchForm();
            hideRegister();
        });
        Tabs tabs = new Tabs(register, showCars, rentACar, returnACar);
        registerUser.setMaxWidth("600px");
        Div registerUserContainer = new Div(registerUser);
        registerUserContainer.setClassName("center-container");
        add(tabs, carSearchForm, grid, registerUserContainer);

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
