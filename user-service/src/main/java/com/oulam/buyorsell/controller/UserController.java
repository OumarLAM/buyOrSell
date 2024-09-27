package com.oulam.buyorsell.controller;

import com.oulam.buyorsell.entity.User;
import com.oulam.buyorsell.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        String response = userService.registerUser(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String email, @RequestParam String password) {
        String token = userService.authenticate(email, password);
        return ResponseEntity.ok(token);    // Return the JWT token on successful login
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String token) {
        // Extract JWT token from the "Bearer" scheme if necessary
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        User userProfile = userService.getUserProfile(token);
        return ResponseEntity.ok(userProfile);
    }
}