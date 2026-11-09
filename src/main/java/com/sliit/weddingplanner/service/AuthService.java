package com.sliit.weddingplanner.service;

import com.sliit.weddingplanner.dto.LoginRequestDTO;
import com.sliit.weddingplanner.dto.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO loginCustomer(LoginRequestDTO loginRequest);
}
