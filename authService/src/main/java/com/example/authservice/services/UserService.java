package com.example.authservice.services;

import com.example.authservice.dto.JwtResponse;
import com.example.authservice.dto.LoginRequest;
import com.example.authservice.dto.RegisterRequest;
import com.example.authservice.dto.ResetPasswordRequest;
import com.example.authservice.models.Role;
import com.example.authservice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.authservice.repositories.RoleRepository;
import com.example.authservice.repositories.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    public User registerUser(RegisterRequest request) {

        User user = mapRequestToUser(request);

        // Назначаем роль по умолчанию
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(new HashSet<>(Set.of(userRole)));

        return userRepository.save(user);
    }

    private User mapRequestToUser(RegisterRequest request) {
        return User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();
    }

    public JwtResponse loginUser(LoginRequest request){

        Authentication authentication = authenticateUser(request);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        Set<GrantedAuthority> authorities = new HashSet<>(authentication.getAuthorities());

        String token = jwtService.generateToken(user.getUsername(),
                (HashSet<GrantedAuthority>) authorities);

        return new JwtResponse(token);
    }

    private Authentication authenticateUser(LoginRequest request) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
    }

    public User getUserFromAuthentication(String token){
        String username = jwtService.extractUsername(deleteHeader(token));
        return findByUsername(username).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void resetPassword(ResetPasswordRequest request) {
        System.out.println("1");
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Пользователь с указанным email не найден"));
        System.out.println("2");
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Новый пароль не должен совпадать с текущим");
        }
        System.out.println("3");
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        System.out.println("4");
        userRepository.save(user);
    }


    public boolean isUsernameAvailable(String username) {
        validateUsername(username);
        return userRepository.findByUsername(username).isEmpty();
    }

    public boolean isEmailAvailable(String email) {
        validateEmail(email);
        return userRepository.findByEmail(email).isEmpty();
    }

    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }
        if (username.length() < 3) {
            throw new IllegalArgumentException("Имя пользователя должно содержать минимум 3 символа");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Некорректный формат email");
        }
    }
    private String deleteHeader(String token){
        return token.substring(7);
    }
}