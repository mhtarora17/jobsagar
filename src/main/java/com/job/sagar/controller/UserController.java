package com.job.sagar.controller;

import com.job.sagar.dto.UsersDataObject;
import com.job.sagar.model.Users;
import com.job.sagar.service.IUsersService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private final IUsersService usersService;

    @Autowired
    public UserController(IUsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public ResponseEntity<List<UsersDataObject>> getAllUsers() {
        List<UsersDataObject> usersDataList = usersService.getAllUsers();
        return ResponseEntity.ok(usersDataList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Optional<Users> user = usersService.getUserById(id);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsersDataObject> createUser(@RequestBody UsersDataObject usersDataObject) {
        UsersDataObject createdUser = usersService.createUser(usersDataObject);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody UsersDataObject userDataObject) {
        Users updatedUser = usersService.updateUser(id, userDataObject);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/ping", method = RequestMethod.GET, produces = "application/json")
    public String ping() {
        return "Service ok";
    }

}