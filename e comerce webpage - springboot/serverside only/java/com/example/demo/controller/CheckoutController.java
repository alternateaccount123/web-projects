package com.example.demo.controller;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.CartItemService;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderEntityService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class CheckoutController {

	@Autowired
    private  CartItemService cartItemService;
	@Autowired
    private  ProductService productService;
	@Autowired
    private  UserService userService;
	@Autowired
    private  OrderEntityService orderService;
	@Autowired 
    private CartService cartService;
    


    @GetMapping("/checkout")
    public String displayCheckout(Model model, Principal principal) {

        // Get the currently logged-in user
        User user = userService.findUserByEmail(principal.getName());   
        Cart cart = cartService.getCartByUser(user);     
        BigDecimal totalCost = cartService.getCartSubtotal(user); 
     
        model.addAttribute("totalCost", totalCost);
        return "checkout";
    }

    //note the order just will keep history of the date but not anything about the cart , still idk how to design that ... without redundancy 
    @PostMapping("/pay")
    public String processCheckout(Principal principal) {
    	System.out.println("pay post handler");
        User user = userService.findUserByEmail(principal.getName());
        LocalDateTime orderDate = LocalDateTime.now();
        OrderEntity order = new OrderEntity(user, orderDate);

        orderService.saveOrder(order);
        cartItemService.clearCart(user);
        
        Cart cart = cartService.getCartByUser(user);     
        cartService.deleteCart(cart);
        
        return "redirect:/checkout-success"; //"redirect:/checkout"
    }
    
    
    
    
    
    //temporarily here ... put it in another controller maybe idk 
    @GetMapping("/checkout-success")    
    public String displayCheckoutSuccess() {
    	System.out.println("pay post handler");
    	return "checkout-success";
    }
}
