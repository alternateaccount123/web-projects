package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    //this didnt work the first time idk why but now it works .requestMatchers(HttpMethod.POST,"/cart/clear").permitAll()  
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/register/**").permitAll()  //
                                .requestMatchers("/index").permitAll()    
                                .requestMatchers("/homepage").permitAll()   //test 
                                .requestMatchers("/search").permitAll()   //test 
                                .requestMatchers("/products").permitAll()
                                .requestMatchers("/cart/list").authenticated() 
                                .requestMatchers("/init/fakeproducts").permitAll() 
                                .requestMatchers("/checkout").authenticated()
                                .requestMatchers("/checkout-success").authenticated()                  
                                .requestMatchers("/pay").authenticated()
                                .requestMatchers("/users").hasRole("ADMIN") //----
                                .requestMatchers("/logoff").permitAll() // Permit access to /logoff
                                .requestMatchers("/css/**").permitAll() // Permit access to CSS files
                                .requestMatchers(HttpMethod.POST,"/cart/add").authenticated()
                                .requestMatchers(HttpMethod.POST,"/cart/clear").authenticated()
                                .requestMatchers(HttpMethod.POST,"/cart/remove").authenticated()
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/index") //original: /users
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //this endpoint not explicitely called, is just config 
                                .logoutSuccessUrl("/login?logoff") // redirect to "/login" endpoint. note if you dont add ?logoff the sessions files might get corrupted just add it 
                                .permitAll()
                );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}