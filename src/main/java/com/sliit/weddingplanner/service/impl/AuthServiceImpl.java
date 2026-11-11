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
    public LoginResponseDTO loginCustomer(LoginRequestDTO loginRequest) {
        String sql = "SELECT customer_id, name, username, email FROM customer "
                + "WHERE (username = ? OR email = ?) AND password = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, loginRequest.getUsername());
            statement.setString(2, loginRequest.getUsername());
            statement.setString(3, loginRequest.getPassword());

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    throw new UnauthorizedException("Invalid customer credentials");
                }

                UserDTO user = new UserDTO();
                user.setId(result.getInt("customer_id"));
                user.setName(result.getString("name"));
                user.setUsername(result.getString("username") != null
                        ? result.getString("username")
                        : result.getString("email"));
                user.setRole("ROLE_CUSTOMER");
                return new LoginResponseDTO("dummy-token-no-security", user.getRole(), user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during customer authentication", e);
        }
    }
}
