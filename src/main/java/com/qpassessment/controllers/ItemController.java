package com.qpassessment.controllers;

import com.qpassessment.entities.Item;
import com.qpassessment.dtos.ItemDto;
import com.qpassessment.dtos.UserDetailDto;
import com.qpassessment.service.AuthenticationService;
import com.qpassessment.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/item/{id}")
    public ResponseEntity getItem(HttpServletRequest request, @PathVariable Long id) throws Exception{
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null){
            Optional<Item> item=itemService.getItem(id);
            if(item.isPresent())
                return ResponseEntity.ok(convertToDto(item.get()));
            else
                return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        else
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/items")
    public ResponseEntity getItems(HttpServletRequest request) throws Exception{

        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null){
            List<ItemDto> itemDtoList=itemService.getItems().stream().map(this::convertToDto).toList();
            return ResponseEntity.ok(itemDtoList);
        }
        else
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/item")
    public ResponseEntity createItem(HttpServletRequest request,@Valid @RequestBody ItemDto itemDto) throws Exception{
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null && userDetailDto.getRole().name() == UserDetailDto.Role.ADMIN.name()){
            Item item=convertToEntity(itemDto);
            Item newItem = itemService.createItem(item);
            ItemDto newItemDto=convertToDto(newItem);
            return ResponseEntity.ok(newItemDto);
        }
        else
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/item")
    public ResponseEntity updateItem(HttpServletRequest request,@Valid @RequestBody ItemDto itemDto) throws Exception{
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null && userDetailDto.getRole().name() == UserDetailDto.Role.ADMIN.name()){
            Item item=convertToEntity(itemDto);
            Item updatedItem = itemService.updateItem(item);
            ItemDto updatedItemDto=convertToDto(updatedItem);
            return ResponseEntity.ok(updatedItemDto);
        }
        else
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity deleteItem(HttpServletRequest request, @PathVariable Long id) throws Exception{
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null && userDetailDto.getRole().name() == UserDetailDto.Role.ADMIN.name()){
            itemService.deleteItem(id);
            return ResponseEntity.ok("Item deleted.");
        }
        else
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    private Item convertToEntity(ItemDto itemDto){
        return modelMapper.map(itemDto,Item.class);
    }

    private ItemDto convertToDto(Item item){
        return modelMapper.map(item,ItemDto.class);
    }
}
