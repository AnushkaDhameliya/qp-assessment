package com.qpassessment.controllers;

import com.qpassessment.dtos.UserDetailDto;
import com.qpassessment.dtos.UserOrderDto;
import com.qpassessment.entities.UserOrder;
import com.qpassessment.service.AuthenticationService;
import com.qpassessment.service.UserOrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class UserOrderController {

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/order/{purchaseId}")
    public ResponseEntity getOrder(HttpServletRequest request, @PathVariable Long purchaseId) throws Exception{
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null){
            List<UserOrder> userOrders =userOrderService.getUserOrder(purchaseId);
            if(!userOrders.isEmpty()) {
                List<UserOrderDto> userOrderDtos=userOrders.stream().map(this::convertToDto).toList();
                return ResponseEntity.ok(userOrderDtos);
            }
            else
                return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        else
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/ordersForUser/{userId}")
    public ResponseEntity getOrdersForUser(HttpServletRequest request, @PathVariable Long userId) throws Exception{
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null){
            Map<Long,List<UserOrder>> userOrdersMap =userOrderService.getUserAllOrders(userId);
            Map<Long,List<UserOrderDto>> userOrderDtoMap=new HashMap<>();
            if(!userOrdersMap.isEmpty()) {
                Set<Long> mapKeys=userOrdersMap.keySet();
                for(Long key:mapKeys){
                    List<UserOrderDto> userOrderDtos=userOrdersMap.get(key).stream().map(this::convertToDto).toList();
                    userOrderDtoMap.put(key,userOrderDtos);
                }
                return ResponseEntity.ok(userOrdersMap);
            }
            else
                return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        else
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/orders")
    public ResponseEntity getOrders(HttpServletRequest request) throws Exception{
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null && userDetailDto.getRole().equals(UserDetailDto.Role.ADMIN)){
            Map<Long,List<UserOrder>> userOrdersMap =userOrderService.getAllUserOrders();
            Map<Long,List<UserOrderDto>> userOrderDtoMap=new HashMap<>();
            if(!userOrdersMap.isEmpty()) {
                Set<Long> mapKeys=userOrdersMap.keySet();
                for(Long key:mapKeys){
                    List<UserOrderDto> userOrderDtos=userOrdersMap.get(key).stream().map(this::convertToDto).toList();
                    userOrderDtoMap.put(key,userOrderDtos);
                }
                return ResponseEntity.ok(userOrdersMap);
            }
            else
                return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        else
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/order")
    public ResponseEntity createOrder(HttpServletRequest request, @RequestBody @Valid List<UserOrderDto> userOrderDtos) throws Exception{
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null && userDetailDto.getRole().equals(UserDetailDto.Role.USER)){
            if(validateUserOrderDates(userOrderDtos)) {
                List<UserOrder> userOrders = userOrderDtos.stream().map(this::convertToEntity).toList();
                boolean valid = userOrderService.validateUserOrder(userOrders);
                if (valid) {
                    userOrders = userOrderService.createUserOrder(userOrders);
                    userOrderDtos = userOrders.stream().map(this::convertToDto).toList();
                    return ResponseEntity.ok(userOrderDtos);
                } else {
                    return ResponseEntity.ok("Invalid Order.");
                }
            }
            return ResponseEntity.ok("Invalid Order dates.");
        }
        else
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/order/{purchaseId}")
    public ResponseEntity deleteUserOrder(HttpServletRequest request, @PathVariable Long purchaseId) throws Exception{
        UserDetailDto userDetailDto = authenticationService.authenticateToken(request);
        if(userDetailDto!=null){
            userOrderService.deleteUserOrder(purchaseId);
            return ResponseEntity.ok("Order deleted.");
        }
        else
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    private UserOrder convertToEntity(UserOrderDto userOrderDto){
        return modelMapper.map(userOrderDto,UserOrder.class);
    }

    private UserOrderDto convertToDto(UserOrder userOrder){
        return modelMapper.map(userOrder,UserOrderDto.class);
    }

    private boolean validateUserOrderDates(List<UserOrderDto> userOrderDtos){
        try {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate orderDate = LocalDate.parse(userOrderDtos.get(0).getOrderedDate().toString(), dateFormat);
            for(UserOrderDto userOrderDto : userOrderDtos){
                LocalDate date = LocalDate.parse(userOrderDto.getOrderedDate().toString(), dateFormat);
                LocalDate current = LocalDate.now();
                if (date.isBefore(current)){
                    return false;
                }
                if(!date.isEqual(orderDate)){
                    return false;
                }
            }
        }catch(DateTimeParseException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }
}
