package com.sliit.weddingplanner.controller.login;

import com.sliit.weddingplanner.dto.LoginRequestDTO;
import com.sliit.weddingplanner.dto.LoginResponseDTO;
import com.sliit.weddingplanner.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/vendor")
public class VendorLoginController {

    private final AuthService authService;

    @Autowired
    public VendorLoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(authService.loginVendor(loginRequest));
    }
}
