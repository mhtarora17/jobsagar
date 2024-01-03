package com.job.sagar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.job.sagar.dto.UsersDataObject;
import com.job.sagar.model.Users;

@Component("usersService")
public interface IUsersService {
    UsersDataObject createUser(UsersDataObject user);
    
    Optional<Users> getUserById(Long id);
    
    List<UsersDataObject> getAllUsers();
    
    Users updateUser(Long id, UsersDataObject user);
    
    void deleteUser(Long id);
}
