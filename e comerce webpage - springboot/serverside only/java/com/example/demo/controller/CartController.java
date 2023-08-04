package com.example.demo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.CartItemService;
import com.example.demo.service.CartService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired 
    private CartItemService cartItemService;
	@Autowired 
    private ProductService productService;
	@Autowired 
    private UserService userService;
	@Autowired 
    private CartService cartService;


    @GetMapping("/list")
    public String displayCart(Model model, Principal principal) {
    	
    	
    	
    	
    	//fix this . is partially wrong . just figure out where to put the set the quantity for the obj using the input 
    	User user = userService.findUserByEmail(principal.getName()); 	        	
    	Cart cart = cartService.getCartByUser(user);     
        
        if (cart == null) {
            // If the cart is null, create an empty cart for the user
            cart = new Cart(user, BigDecimal.ZERO);
            cartService.saveCart(cart);
        }
        
        
        List<CartItem> cartItems = cart.getCartItems();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("subtotal", cart.getSubtotal()); //is ok to pass primitive to thy 
        model.addAttribute("mySearchIcon", "https://i.imgur.com/9OkEdaM.png"); //HARCODED CHANGE LATER ...
        
        return "cart";
    }

    //this works for the cart implementation now just update the subtotal from the cart 
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity, Principal principal) {
        
        User user = userService.findUserByEmail(principal.getName()); //get user
        Product product = productService.getProductById(productId);  //get product by product id

        if (product == null) {
            return "redirect:/error";
        }

        // Check if product is available
        if (product.getQuantity_available() >= quantity) {

            // If the requested quantity is available, decrement the quantity immediately
            product.setQuantity_available(product.getQuantity_available() - quantity);
            productService.saveProduct(product);

            //has this user a cart.
            Cart cart = cartService.getCartByUser(user); 
            //no.
            if (cart == null) {
                cart = new Cart(user, BigDecimal.ZERO); //make cart. 
            }
            
            //has this user this product as a cartItem. 
            CartItem existingCartItem = cartItemService.getCartItemByUserAndProduct(user, product);
            //yes.
            if (existingCartItem != null) {
                //update cart item
                existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity); // increase the quantity
                cartItemService.saveCartItem(existingCartItem);
            } else { //no.
                //make cart item
                CartItem newCartItem = new CartItem(user, product, quantity);
                cartItemService.saveCartItem(newCartItem);
                cart.addCartItem(newCartItem); // Add cart item to cart 
            }

            cartService.saveCart(cart); //save cart after updating its values, cart items wont be available in service if you dont use this here 
                                     
            //calculate subtotal inside service          
            BigDecimal subtotal = cartService.getCartSubtotal(user); 
            cart.setSubtotal(subtotal);
            cartService.saveCart(cart);  //save cart after updating its values
            
        } else {
            // Handle insufficient quantity scenario (e.g., redirect to a notification page)
            return "redirect:/insufficient-quantity";
        }

        return "redirect:/products"; // Redirect bac
    }
    
    @PostMapping("/clear")
    public String clearCart(Principal principal) {
        // Get the currently logged-in user
        User user = userService.findUserByEmail(principal.getName());

        // Get all cart items for the user
        Iterable<CartItem> cartItems = cartItemService.getCartItemsByUser(user);

        // Reset the quantity of each product in the cart and delete the cart items
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            // Increase the quantity_available of the product by the quantity in the cart item
            int quantityInCart = cartItem.getQuantity();
            product.setQuantity_available(product.getQuantity_available() + quantityInCart);
            // Save the updated product
            productService.saveProduct(product);
            // Delete the cart item
            cartItemService.deleteCartItem(cartItem);
        }
        
        //delete the cart 
        Cart cart = cartService.getCartByUser(user);
        cartService.deleteCart(cart);
        
        return "redirect:/cart/list"; // Redirect back to the cart page after clearing the cart.
    }
    
    //this works now and idk why ... i did no changes ...and works now 
    @PostMapping("/remove")
    public String removeCartItem(@RequestParam("cartItemId") Long cartItemId, Principal principal) {
    	System.out.println("inside the post method remove... cart item >>>>>>>>>>>>>>>>>>>>>>>>>");
    	
        // Get the currently logged-in user
        User user = userService.findUserByEmail(principal.getName());

        // Find the cart item by ID
        CartItem cartItem = cartItemService.getCartItemById(cartItemId);
        System.out.println(cartItem.getProduct().getName());

        // Ensure the cart item belongs to the logged-in user
        if (cartItem != null && cartItem.getUser().getId().equals(user.getId())) {
            Product product = cartItem.getProduct();
            System.out.println(product.getName());
            
            // Increase the quantity_available of the product
            int quantityInCart = cartItem.getQuantity();
            product.setQuantity_available(product.getQuantity_available() + quantityInCart); //update product quantity

            productService.saveProduct(product);  //save product update 
            cartItemService.deleteCartItem(cartItem); //delete cartItem  
            
            //updated based on the items removed ...
            //calculate subtotal inside service 
            Cart cart = cartService.getCartByUser(user);
            BigDecimal subtotal = cartService.getCartSubtotal(user); 
            cart.setSubtotal(subtotal);
            cartService.saveCart(cart);  //save cart after updating its values
            
        }else {
        	System.out.println("error");
        }
        
        return "redirect:/cart/list"; // Redirect back to the cart page after clearing the cart.
    }
    
    
    

    
    
    

}
