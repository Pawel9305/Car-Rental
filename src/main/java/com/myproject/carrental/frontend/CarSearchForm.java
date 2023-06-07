package com.myproject.carrental.frontend;

import com.myproject.carrental.domain.Car;
import com.myproject.carrental.domain.CarDto;
import com.myproject.carrental.service.CarService;
import com.myproject.carrental.service.RentalService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
public class CarSearchForm extends FormLayout {
    private CarService carService;
    private MainView mainView;
    private TextField brand = new TextField("Brand");
    private TextField model = new TextField("Model");
    private ComboBox<CarType> typeBox = new ComboBox<>("Type");
    private ComboBox<City> location = new ComboBox<>("Location");

    public CarSearchForm(MainView mainView, CarService carService) {
        this.carService = carService;
        this.mainView = mainView;
        brand.setClearButtonVisible(true);
        brand.setValueChangeMode(ValueChangeMode.EAGER);
        brand.addValueChangeListener(e -> searchForCars());
        model.setClearButtonVisible(true);
        model.setValueChangeMode(ValueChangeMode.EAGER);
        model.addValueChangeListener(e -> searchForCars());
        typeBox.setItems(CarType.values());
        typeBox.addValueChangeListener(e -> searchForCars());
        location.setItems(City.values());
        location.addValueChangeListener(e -> searchForCars());

        add(brand, model, typeBox, location);

    }

    private void searchForCars() {
        String typeFromTypeBox = typeBox.getValue() != null ? typeBox.getValue().toString().toLowerCase() : "";
        String locationFromBox = location.getValue() != null ? location.getValue().toString().toLowerCase() : "";
        log.info("starting to filter");

        List<CarDto> filteredCars = carService.getAllCars().stream()
                .filter(car -> car.getBrand().toLowerCase().contains(brand.getValue().toLowerCase()))
                .filter(car -> car.getModel().toLowerCase().contains(model.getValue().toLowerCase()))
                .filter(car -> car.getType().toLowerCase().contains(typeFromTypeBox))
                .filter(car -> car.getLocation().toLowerCase().contains(locationFromBox))
                .toList();
        log.info("size of a founded list = " + filteredCars.size());
        mainView.updateFilteredCars(filteredCars);
    }
}
