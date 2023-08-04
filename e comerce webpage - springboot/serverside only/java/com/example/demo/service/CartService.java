package com.example.demo.service;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;


    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
    
    
    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

    
    
    // Get the total cost of the cart for a given user
    public BigDecimal getCartSubtotal(User user) {
        Cart cart = getCartByUser(user);
        //System.out.println("cart: " + cart);
        
        //if (cart != null) {
        	//System.out.println("cart exist");
            List<CartItem> cartItems = cart.getCartItems();
            BigDecimal totalCost = BigDecimal.ZERO;

            for (CartItem cartItem : cartItems) {
            	//System.out.println("inside iteration");
                Product product = cartItem.getProduct();
                BigDecimal price = product.getPrice();
                int quantity = cartItem.getQuantity();
                BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));
                totalCost = totalCost.add(subtotal);
            }
            //System.out.println("totalCost =" + totalCost);
            return totalCost;
        //}

       // return BigDecimal.ZERO; // If the cart is empty or not found, return 0.
    }
    
    
    public void deleteCart(Cart cart) {
        cartRepository.delete(cart);
    }
    
    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }



}
