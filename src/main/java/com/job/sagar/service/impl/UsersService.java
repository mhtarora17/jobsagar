package com.job.sagar.service.impl;

import com.job.sagar.dto.UsersDataObject;
import com.job.sagar.model.Users;
import com.job.sagar.repository.UsersRepository;
import com.job.sagar.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;

@Service
public class UsersService implements IUsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository userRepository) {
        this.usersRepository = userRepository;
    }

    @Override
    public List<UsersDataObject> getAllUsers() {
        List<Users> usersList = usersRepository.findAll();
        List<UsersDataObject> usersDataList = new ArrayList<>();

        for (Users user : usersList) {
            UsersDataObject userData = new UsersDataObject();
            BeanUtils.copyProperties(user, userData);
            usersDataList.add(userData);
        }
        return usersDataList;
    }

    @Override
    public Optional<Users> getUserById(Long id) {
        return usersRepository.findById(id);
    }

    @Override
    public UsersDataObject createUser(UsersDataObject usersDataObject) {
        Users users = new Users();
        BeanUtils.copyProperties(usersDataObject, users);
        usersRepository.save(users);
        return usersDataObject;
    }
    

    @Override
    public Users updateUser(Long id, UsersDataObject userDataObject) {
        Optional<Users> existingUser = usersRepository.findById(id);
        if (existingUser.isPresent()) {
            userDataObject.setId(id);
            BeanUtils.copyProperties(userDataObject, existingUser, getClass());
            return usersRepository.save(existingUser.get());
        } else {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
    }

    @Override
    public void deleteUser(Long id) {
        usersRepository.deleteById(id);
    }
}
