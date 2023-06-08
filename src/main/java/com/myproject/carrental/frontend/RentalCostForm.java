package com.myproject.carrental.frontend;

import com.myproject.carrental.client.ExchangeRatesClient;
import com.myproject.carrental.domain.AdditionalEquipmentDto;
import com.myproject.carrental.domain.CarDto;
import com.myproject.carrental.service.EquipmentService;
import com.myproject.carrental.service.ExchangeRatesService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;

import java.math.BigDecimal;
import java.util.Set;

public class RentalCostForm extends FormLayout {

    private EquipmentService equipmentService;
    private CheckboxGroup<AdditionalEquipmentDto> additionalEqChoice = new CheckboxGroup<>("Optional equipment");
    private ComboBox<String> currency = new ComboBox<>("Payment currency");
    private Button calculateAmountOfCurrency = new Button("Calculate to PLN");
    private Label cost = new Label();
    private BigDecimal totalCost;
    private CarDto carDto;


    public RentalCostForm(CarDto carDto, BigDecimal totalCost, EquipmentService equipmentService) {
        this.carDto = carDto;
        this.totalCost = totalCost;
        this.equipmentService = equipmentService;


        cost.setText("Total cost: " + totalCost.toString() + " pln");
        additionalEqChoice.setItems(equipmentService.getAllElements());
        additionalEqChoice.setItemLabelGenerator(equipment -> equipment.getDescription() + " - " + equipment.getPrice() + " pln");
        additionalEqChoice.addValueChangeListener(e -> {
            BigDecimal additionalEquipmentCost = getAdditionalCost();
            cost.setText("Total cost: " + totalCost.add(additionalEquipmentCost));
        });
        calculateAmountOfCurrency.addClickListener(e -> {
            String chosenCurrency = currency.getValue();
        });
        currency.setItems("EUR", "USD", "CZK");
        add(cost, additionalEqChoice, currency, calculateAmountOfCurrency);
    }

    public BigDecimal getAdditionalCost() {
        BigDecimal additionalEquipmentCost = BigDecimal.ZERO;
        Set<AdditionalEquipmentDto> selectedEquipment = additionalEqChoice.getSelectedItems();
        for (AdditionalEquipmentDto equipment : selectedEquipment) {
            additionalEquipmentCost = additionalEquipmentCost.add(equipment.getPrice());
        }
        return additionalEquipmentCost;
    }

    public String getPaymentCurrency() {
        return currency.getValue();
    }
}
