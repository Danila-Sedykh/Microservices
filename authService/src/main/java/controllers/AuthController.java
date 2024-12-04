package controllers;


import models.JwtResponse;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Сохрани пользователя в БД (пока можно не реализовывать)
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody User user) {
        // Проверь пользователя в БД (пока можно не реализовывать)
        String token = jwtService.generateToken(user.getUsername());
        return new JwtResponse(token);
    }
}
