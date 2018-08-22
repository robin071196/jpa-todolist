package com.example.postgresdemo.controller;

import com.example.postgresdemo.exception.ResourceNotFoundException;
import com.example.postgresdemo.model.User;
import com.example.postgresdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user")
    public long getUser(Pageable pageable) {
        return userRepository.findAll(pageable).getTotalElements();
    }

    @PostMapping("/user/register")
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
    public List<User> getUserByUsername(@PathVariable String username) {
        if(!userRepository.existsById(username)) {
            throw new ResourceNotFoundException("Username " + username + " not found!");
        }

        return userRepository.findByUsername(username);
    }

    @PutMapping("/user/{username}/update")
    public User updateUser(@PathVariable String username,
                                   @Valid @RequestBody User userRequest) {
        return userRepository.findById(username)
                .map(user -> {
                    user.setName(userRequest.getName());
                    return userRepository.save(user);
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + username));
    }

    @DeleteMapping("/user/{username}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        return userRepository.findById(username)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + username));
    }
}