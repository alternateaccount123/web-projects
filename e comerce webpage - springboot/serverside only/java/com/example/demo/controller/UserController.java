package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.UserDTO;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import java.util.List;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    
    @GetMapping("/index")
    public String home(Model model) {
    	System.out.println("<<<<<<<<<<<<<<</index<<<<<<<<<<<<<<<<<<<<<<<<<" + SecurityContextHolder.getContext().getAuthentication() + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"); 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
        model.addAttribute("product", new Product()); //test post endpoint      
        return "index";
    }

    // handler method to handle login request
    @GetMapping("/login")
    public String login(){
    	System.out.println("<<<<<<<<<<<<<<</login<<<<<<<<<<<<<<<<<<<<<<<<<" + SecurityContextHolder.getContext().getAuthentication() + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"); 
        return "login";
    }

    // handler method to handle user registration form request
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        // create model object to store form data
        UserDTO user = new UserDTO();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle user registration form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDTO userDto,BindingResult result, Model model){
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }
        	
        userService.saveUser(userDto);
        return "redirect:/register?success";
    }
    
    //the current problem is that this is never called 
    @GetMapping("/logoff")
    public String logoff(HttpServletRequest request, HttpServletResponse response) {
    	System.out.println("logoff endpoint-------");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logoff";
    }
    
    //passing context variables into thymeleaf (this is updated code.)
    @ModelAttribute("userLoggedIn")
    public boolean isUserLoggedIn(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }
    @ModelAttribute("username")
    public String getUsername(Authentication authentication) {
        return authentication != null ? authentication.getName() : null;
    }


}
