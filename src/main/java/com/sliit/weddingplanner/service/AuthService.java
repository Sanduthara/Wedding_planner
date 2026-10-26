package com.sliit.weddingplanner.service;

import com.sliit.weddingplanner.dto.LoginRequestDTO;
import com.sliit.weddingplanner.dto.LoginResponseDTO;

// OOP: Interface-based Design
// OOP: Abstraction
public interface AuthService {
    LoginResponseDTO authenticate(LoginRequestDTO loginRequest);
    LoginResponseDTO loginAdmin(LoginRequestDTO loginRequest);
    LoginResponseDTO loginCustomer(LoginRequestDTO loginRequest);
    LoginResponseDTO loginVendor(LoginRequestDTO loginRequest);
}
