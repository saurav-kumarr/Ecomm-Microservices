package com.ecommerce.order.services;

import com.ecommerce.order.clients.ProductServiceClient;
import com.ecommerce.order.clients.UserServiceClient;
import com.ecommerce.order.dtos.CartItemRequest;
import com.ecommerce.order.dtos.ProductResponse;
import com.ecommerce.order.dtos.UserResponse;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.repositories.CartItemRepository;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    //private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    //private final UserRepository userRepository;


    //@CircuitBreaker(name = "productService", fallbackMethod = "addToCartFallBack")
    @Retry(name = "retryBreaker", fallbackMethod = "addToCartFallBack")
    public boolean addToCart(String userId, CartItemRequest request) {

        // Look for product
        ProductResponse productResponse = productServiceClient.getProductDetails(request.getProductId());
        if(productResponse == null || productResponse.getStockQuantity()< request.getQuantity()) {
            return false;
        }

        UserResponse userResponse = userServiceClient.getUserDetails(userId);
        if(userResponse == null) {
            return false;
        }

       /*  Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()){
            return false;
        }

        User user = userOpt.get();*/

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId,request.getProductId());
        if(existingCartItem != null){
            // Update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(existingCartItem);
        } else {
            // Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(cartItem);
        }

        return true;

    }

    public boolean addToCartFallBack(String userId, CartItemRequest request,
                                     Exception exception){
        System.out.println("FALLBACK CALLED");
        return false;
    }

    public boolean deleteItemFromCart(String userId, String productId) {

        // Look for product
        /*Optional<Product> productOpt = productRepository.findById(productId);
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));*/

        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if(cartItem != null){
            cartItemRepository.delete(cartItem);
            return true;
        }
            return false;
    }

    public List<CartItem> getCart(String userId) {

        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
