package com.qpassessment.controllers;

import com.qpassessment.entities.Inventory;
import com.qpassessment.dtos.InventoryDto;
import com.qpassessment.dtos.UserDetailDto;
import com.qpassessment.service.AuthenticationService;
import com.qpassessment.service.InventoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/inventory/{id}")
    public ResponseEntity getInventory(HttpServletRequest request, @PathVariable Long id) throws Exception{
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null){
            Optional<Inventory> inventory=inventoryService.getInventory(id);
            if(inventory.isPresent()) {
                InventoryDto inventoryDto=convertToDto(inventory.get());
                return ResponseEntity.ok(inventoryDto);
            }
            else
                return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        else
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/inventories")
    public ResponseEntity getInventories(HttpServletRequest request) throws Exception{
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null){
            List<Inventory> inventories=inventoryService.getInventories();
            List<InventoryDto> inventoryDtoList=inventories.stream().map(this::convertToDto).toList();
            return ResponseEntity.ok(inventoryDtoList);
        }
        else
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/inventoryLevels")
    public ResponseEntity getInventoryLevels(HttpServletRequest request) throws Exception{
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null && userDetailDto.getRole().name() == UserDetailDto.Role.ADMIN.name()){
            Map<String,Set<Inventory>> inventories=inventoryService.getInventoryLevels();
            Map<String,Set<InventoryDto>> inventoryDtoMap=new HashMap<>();
            Set<String> mapKeys=inventories.keySet();
            for(String key:mapKeys){
                Set<InventoryDto> inventoryDtoSet = inventories.get(key).stream().map(this::convertToDto).collect(Collectors.toSet());
                inventoryDtoMap.put(key,inventoryDtoSet);
            }
            return ResponseEntity.ok(inventoryDtoMap);
        }
        else
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/inventory")
    public ResponseEntity createInventory(HttpServletRequest request,@Valid @RequestBody InventoryDto inventoryDto) throws Exception{
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null && userDetailDto.getRole().name() == UserDetailDto.Role.ADMIN.name()){
            Inventory inventory=convertToEntity(inventoryDto);
            Inventory newInventory=inventoryService.createInventory(inventory);
            if(newInventory==null)
                return ResponseEntity.ok("Inventory not created. Item and Stocked date combination is not unique. Provide different stocked date.");
            InventoryDto newInventoryDto=convertToDto(newInventory);
            return ResponseEntity.ok(newInventoryDto);
        }
        else
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/inventory")
    public ResponseEntity updateInventory(HttpServletRequest request,@Valid @RequestBody InventoryDto inventoryDto) throws Exception{
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null && userDetailDto.getRole().name() == UserDetailDto.Role.ADMIN.name()){
            Inventory inventory=convertToEntity(inventoryDto);
            Inventory updatedInventory=inventoryService.updateInventory(inventory);
            if(updatedInventory==null)
                return ResponseEntity.ok("Inventory not updated. Item and Stocked date combination is not unique. Provide different stocked date.");
            InventoryDto updatedInventoryDto=convertToDto(updatedInventory);
            return ResponseEntity.ok(updatedInventoryDto);
        }
        else
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/inventory/{id}")
    public ResponseEntity deleteInventory(HttpServletRequest request, @PathVariable Long id) throws Exception{
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null && userDetailDto.getRole().name() == UserDetailDto.Role.ADMIN.name()){
            inventoryService.deleteInventory(id);
            return ResponseEntity.ok("Inventory deleted.");
        }
        else
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    private Inventory convertToEntity(InventoryDto inventoryDto){
        return modelMapper.map(inventoryDto,Inventory.class);
    }

    private InventoryDto convertToDto(Inventory inventory){
        return modelMapper.map(inventory,InventoryDto.class);
    }
}
