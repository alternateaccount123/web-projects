package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;

@Controller
@RequestMapping("/init")
public class InitDummyProductsController {

    private final ProductService productService;

    @Autowired
    public InitDummyProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/fakeproducts")
    public String initializeDummyProducts() {
                                                                                                             
    	Product dummyProduct = new Product("novaSprint","High-quality shoes for everyday use", BigDecimal.valueOf(81.65), 500, "https://i.imgur.com/0lK3AWy.png");
    	productService.saveProduct(dummyProduct);
    	
    	Product dummyProduct2 = new Product("luminaGlide z3000","this is the description of the product", BigDecimal.valueOf(40.45),500, "https://i.imgur.com/DCO6fTd.png");
    	productService.saveProduct(dummyProduct2);
    	
    	Product dummyProduct3 = new Product("asdf 123 car","this is the description of the product", BigDecimal.valueOf(21.55),500, "https://via.placeholder.com/200");
    	productService.saveProduct(dummyProduct3);
    	
    	Product dummyProduct4 = new Product("car model123","this is the description of the product", BigDecimal.valueOf(38.50),500, "https://via.placeholder.com/200");
    	productService.saveProduct(dummyProduct4);
    	
    	Product dummyProduct5 = new Product("car w77","this is the description of the product", BigDecimal.valueOf(21.00),500, "https://via.placeholder.com/200");
    	productService.saveProduct(dummyProduct5);
    	
    	return "redirect:/products";
    }
}
