package com.example.demo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



@Entity
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    // The user who owns this cart
    @OneToOne
    private User user;

    // The cart items associated with this cart
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<CartItem>(); // Use List instead of Set

    private BigDecimal subtotal;
    
    public Cart() {
    }



	public Cart(User user, BigDecimal subtotal) {
		super();
		this.user = user;
		this.subtotal = subtotal;
	}



	public Cart(User user, List<CartItem> cartItems, BigDecimal subtotal) {
		super();
		this.user = user;
		this.cartItems = cartItems;
		this.subtotal = subtotal;
	}



	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	//getter
	public List<CartItem> getCartItems() {
		return cartItems;
	}
	
	//helper method 
	public void addCartItem(CartItem cartItem) {
		cartItem.setCart(this); // stablish relationship with parent entity 
		if(this.cartItems == null) {
			this.cartItems = new ArrayList<>();
		}
		this.cartItems.add(cartItem);
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}



    

}
