package com.example.authservice.controllers;


import com.example.authservice.dto.JwtResponse;
import com.example.authservice.dto.LoginRequest;
import com.example.authservice.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.authservice.services.JwtService;
import com.example.authservice.services.UserService;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/register")
        public String register(@RequestBody RegisterRequest request) {
        userService.registerUser(request.getUsername(), request.getPassword());
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtService.generateToken(authentication.getName(),new HashSet<GrantedAuthority>( authentication.getAuthorities()));
        return new JwtResponse(token);
    }

    /*@PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Сохрани пользователя в БД (пока можно не реализовывать)
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody User user) {
        // Проверь пользователя в БД (пока можно не реализовывать)
        List<String> roles = List.of("ROLE_USER"); // Получить роли из БД
        String token = jwtService.generateToken(user.getUsername(), roles);
        return new JwtResponse(token);
    }*/
}
