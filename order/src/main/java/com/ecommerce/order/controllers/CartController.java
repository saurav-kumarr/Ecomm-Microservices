package com.ecommerce.order.controllers;

import com.ecommerce.order.dtos.CartItemRequest;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.repositories.CartItemRepository;
import com.ecommerce.order.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartItemRepository cartItemRepository;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest request){
       if( !cartService.addToCart(userId, request)){
           return ResponseEntity.badRequest()
                   .body("Not able to complete the request");
       }
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");

    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void>  removeFromCart(
            @RequestHeader("X-User-ID") String userId,
           @PathVariable String productId){

       boolean deleted =  cartService.deleteItemFromCart(userId,productId);

       return deleted ? ResponseEntity.noContent().build()
               : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            @RequestHeader("X-User-ID") String userId){
        return ResponseEntity.ok(cartService.getCart(userId));
    }

}
