package com.example.demo.service;



import java.util.List;

import com.example.demo.UserDTO;
import com.example.demo.entity.User;

public interface UserService {
    void saveUser(UserDTO userDto);

    User findUserByEmail(String email);

    List<UserDTO> findAllUsers();

	
}


