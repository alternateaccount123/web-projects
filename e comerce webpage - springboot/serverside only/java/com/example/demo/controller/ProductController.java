package com.example.demo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;

@Controller
public class ProductController {

		
	@Autowired 
	private ProductService productService;  //bean injection , you could also use constructor injection , they are eq

    //because of NO use of query parameter this can be accessed normally
    //like this  http://localhost:8080/homepage 
    @GetMapping("/products")
    public String showProductList(Model model) {
        List<Product> products = productService.getAllProducts(); //get all products from db 
        model.addAttribute("products", products);
        model.addAttribute("mySearchIcon", "https://i.imgur.com/9OkEdaM.png"); //HARCODED 3 TIMES, CHANGE LATER ... 
        return "product-list"; //product-list.html
    }
    

    //because of the query parameter this can only be accessed
    //like this  http://localhost:8080/search?query=<yourquery>       this WILL NOT work http://localhost:8080/search
    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {  
    	List<Product> searchResults = productService.searchProducts(query);
    	model.addAttribute("results", searchResults);
        model.addAttribute("query", query);//use query parameter 
        model.addAttribute("mySearchIcon", "https://i.imgur.com/9OkEdaM.png"); //HARCODED CHANGE LATER ...
        return "product-list";  //load homepage  
    }

    
}
