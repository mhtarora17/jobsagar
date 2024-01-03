package com.job.sagar.service.impl;

import com.job.sagar.model.Users;
import com.job.sagar.repository.UsersRepository;
import com.job.sagar.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService implements IUsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository userRepository) {
        this.usersRepository = userRepository;
    }

    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public Optional<Users> getUserById(Long id) {
        return usersRepository.findById(id);
    }

    @Override
    public Users createUser(Users user) {
        return usersRepository.save(user);
    }

    @Override
    public Users updateUser(Long id, Users user) {
        Optional<Users> existingUser = usersRepository.findById(id);
        if (existingUser.isPresent()) {
            user.setId(id);
            return usersRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
    }

    @Override
    public void deleteUser(Long id) {
        usersRepository.deleteById(id);
    }
}
