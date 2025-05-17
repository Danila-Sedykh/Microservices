package com.example.authservice.dto;

import com.example.authservice.models.Role;
import lombok.Data;

import java.util.Set;

@Data
public class JwtRequest {

    private String username;

    private Set<Role> roles;
}
