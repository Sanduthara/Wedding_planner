package com.sliit.weddingplanner.service.impl;

import com.sliit.weddingplanner.db.DBConnection;
import com.sliit.weddingplanner.dto.LoginRequestDTO;
import com.sliit.weddingplanner.dto.LoginResponseDTO;
import com.sliit.weddingplanner.dto.UserDTO;
import com.sliit.weddingplanner.exception.UnauthorizedException;
import com.sliit.weddingplanner.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class AuthServiceImpl implements AuthService {

    private final DBConnection dbConnection;

    @Autowired
    public AuthServiceImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // Check Admin
        UserDTO user = checkUser("admin", "admin_id", username, password, "ROLE_ADMIN");
        if (user != null) return createResponse(user);

        // Check Customer
        user = checkUser("customer", "customer_id", username, password, "ROLE_CUSTOMER");
        if (user != null) return createResponse(user);

        // Check Vendor
        user = checkUser("vendor", "vendor_id", username, password, "ROLE_VENDOR");
        if (user != null) return createResponse(user);

        throw new UnauthorizedException("Invalid username or password");
    }

    @Override
    public LoginResponseDTO loginAdmin(LoginRequestDTO loginRequest) {
        UserDTO user = checkUser("admin", "admin_id", loginRequest.getUsername(), loginRequest.getPassword(), "ROLE_ADMIN");
        if (user != null) return createResponse(user);
        throw new UnauthorizedException("Invalid admin credentials");
    }

    @Override
    public LoginResponseDTO loginCustomer(LoginRequestDTO loginRequest) {
        UserDTO user = checkUser("customer", "customer_id", loginRequest.getUsername(), loginRequest.getPassword(), "ROLE_CUSTOMER");
        if (user != null) return createResponse(user);
        throw new UnauthorizedException("Invalid customer credentials");
    }

    @Override
    public LoginResponseDTO loginVendor(LoginRequestDTO loginRequest) {
        UserDTO user = checkUser("vendor", "vendor_id", loginRequest.getUsername(), loginRequest.getPassword(), "ROLE_VENDOR");
        if (user != null) return createResponse(user);
        throw new UnauthorizedException("Invalid vendor credentials or account not approved");
    }

    private UserDTO checkUser(String table, String idCol, String username, String password, String role) {
        String sql = String.format("SELECT %s as id, name, username, email FROM %s WHERE (username = ? OR email = ?) AND password = ?", idCol, table);
        if (table.equals("vendor")) {
            sql += " AND status = 'APPROVED'";
        }

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, username);
            ps.setString(3, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UserDTO user = new UserDTO();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setUsername(rs.getString("username") != null ? rs.getString("username") : rs.getString("email"));
                user.setRole(role);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during auth", e);
        }
        return null;
    }

    private LoginResponseDTO createResponse(UserDTO user) {
        // Return a dummy token since security is removed but frontend expects one
        return new LoginResponseDTO("dummy-token-no-security", user.getRole(), user);
    }
}
