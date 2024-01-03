package com.job.sagar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.job.sagar.model.Users;

@Component("usersService")
public interface IUsersService {
    Users createUser(Users user);
    
    Optional<Users> getUserById(int id);
    
    List<Users> getAllUsers();
    
    Users updateUser(Integer id, Users user);
    
    void deleteUser(int id);
}
