package com.myproject.carrental.frontend;

import com.myproject.carrental.client.ExchangeRatesClient;
import com.myproject.carrental.domain.AdditionalEquipmentDto;
import com.myproject.carrental.domain.CarDto;
import com.myproject.carrental.service.EquipmentService;
import com.myproject.carrental.service.ExchangeRatesService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
public class RentalForm extends FormLayout {

    private EquipmentService equipmentService;
    private ExchangeRatesClient exchangeRatesClient;
    private ExchangeRatesService exchangeRatesService;
    private List<Long> selectedEquipmentIds;
    private final CheckboxGroup<AdditionalEquipmentDto> additionalEqChoice = new CheckboxGroup<>("Optional equipment");
    private ComboBox<String> currency = new ComboBox<>("Payment currency");
    private Button calculateAmountOfCurrency = new Button("Calculate");
    private Label cost = new Label();
    private Label convertedCost = new Label();
    private BigDecimal convertedAmount;
    private BigDecimal totalCost;
    private CarDto carDto;


    public RentalForm(CarDto carDto, BigDecimal totalCost, EquipmentService equipmentService,
                      ExchangeRatesService exchangeRatesService, ExchangeRatesClient exchangeRatesClient) {
        this.carDto = carDto;
        this.totalCost = totalCost;
        this.equipmentService = equipmentService;
        this.exchangeRatesService = exchangeRatesService;
        this.exchangeRatesClient = exchangeRatesClient;

        convertedCost.setVisible(false);
        cost.setText("Total cost: " + totalCost.toString() + " PLN");
        additionalEqChoice.setItems(equipmentService.getAllElements());
        additionalEqChoice.setItemLabelGenerator(equipment -> equipment.getDescription() + " - " + equipment.getPrice() + " PLN");

        additionalEqChoice.addValueChangeListener(e -> cost.setText("Total cost: " + totalCost.add(getAdditionalCost())));
        calculateAmountOfCurrency.addClickListener(e -> {
            String chosenCurrency = currency.getValue();
            convertedAmount = exchangeRatesService.getAmountToPay("PLN", chosenCurrency, totalCost.add(getAdditionalCost()));
            convertedCost.setText("Total cost in your currency is:   " + convertedAmount);
            convertedCost.setVisible(true);
        });

        currency.setItems("PLN", "EUR", "USD", "JPY", "AUD", "CHF", "GBP");
        add(cost, additionalEqChoice, currency, calculateAmountOfCurrency, convertedCost);
    }

    public BigDecimal getAdditionalCost() {
        BigDecimal additionalEquipmentCost = BigDecimal.ZERO;
        selectedEquipmentIds = additionalEqChoice.getSelectedItems().stream()
                .map(AdditionalEquipmentDto::getId)
                .toList();

        Set<AdditionalEquipmentDto> selectedEquipment = additionalEqChoice.getSelectedItems();
        for (AdditionalEquipmentDto equipment : selectedEquipment) {
            additionalEquipmentCost = additionalEquipmentCost.add(equipment.getPrice());
        }
        return additionalEquipmentCost;
    }
}
