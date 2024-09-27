package com.oulam.buyorsell.service;

import com.oulam.buyorsell.entity.User;
import com.oulam.buyorsell.repository.UserRepository;
import com.oulam.buyorsell.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully";
    }

    public String authenticate(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            // Generate JWT token
            return jwtUtil.generateToken(user.getEmail());
        }
        throw new RuntimeException("Invalid credentials");
    }

    public User getUserProfile(String token) {
        // Extract the username (email) from the JWT token
        String email = jwtUtil.extractUsername(token);

        // Retrieve the user based on the email
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElseThrow(() -> new RuntimeException("User not found"));
    }
}
