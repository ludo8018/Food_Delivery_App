package com.tap.dao;

import java.util.List;

import com.tap.model.User;

public interface UserDAO {

    void addUser(User user);

    User getUserById(int userId);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    User login(String email, String password);

    void updateUser(User user);
    
    //boolean updateUser(User user);

    void deleteUser(int userId);

    List<User> getAllUsers();
}