package com.myproject.carrental.controller;


import com.myproject.carrental.domain.AdditionalEquipmentDto;
import com.myproject.carrental.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("v1/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @PostMapping(value = "add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addEquipment(@RequestBody AdditionalEquipmentDto additionalEquipmentDto) {
        equipmentService.addElement(additionalEquipmentDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<AdditionalEquipmentDto>> findAllElements() {
        return ResponseEntity.ok(equipmentService.getAllElements());
    }

    @DeleteMapping("delete/{elementId}")
    public ResponseEntity<Void> deleteChosenElement(@PathVariable("elementId") long id) {
        equipmentService.deleteElement(id);
        return ResponseEntity.ok().build();
    }
}
