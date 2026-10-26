package com.sliit.weddingplanner.repository;

import com.sliit.weddingplanner.db.DBConnection;
import com.sliit.weddingplanner.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: CustomerRepository depends on DBConnection
@Repository
public class CustomerRepository {

    private final DBConnection dbConnection;

    @Autowired
    public CustomerRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public CustomerDTO save(CustomerDTO customerDTO) {
        String sql = "INSERT INTO customer (title, name, username, customer_role, other_party_name, email, phone, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, customerDTO.getTitle());
            ps.setString(2, customerDTO.getName());
            ps.setString(3, customerDTO.getUsername());
            ps.setString(4, customerDTO.getCustomerRole());
            ps.setString(5, customerDTO.getOtherPartyName());
            ps.setString(6, customerDTO.getEmail());
            ps.setString(7, customerDTO.getPhone());
            ps.setString(8, customerDTO.getPassword());

            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    customerDTO.setId(rs.getInt(1));
                }
            }
            return customerDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving customer", e);
        }
    }

    public Optional<CustomerDTO> findById(int id) {
        String sql = "SELECT * FROM customer WHERE customer_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToCustomer(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding customer by ID", e);
        }
        return Optional.empty();
    }

    public List<CustomerDTO> findAll() {
        List<CustomerDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRowToCustomer(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all customers", e);
        }
        return list;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM customer WHERE email=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking customer existence", e);
        }
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM customer WHERE username=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking customer username existence", e);
        }
    }

    public boolean existsByPhone(String phone) {
        String sql = "SELECT 1 FROM customer WHERE phone=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking customer phone existence", e);
        }
    }

    public void delete(int id) {
        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 1. Delete reviews
                String sql1 = "DELETE FROM review WHERE customer_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql1)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }

                // 2. Handle bookings (need to delete payments and booking_package first)
                String fetchBookings = "SELECT booking_id FROM booking WHERE customer_id = ?";
                List<Integer> bookingIds = new ArrayList<>();
                try (PreparedStatement ps = conn.prepareStatement(fetchBookings)) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) bookingIds.add(rs.getInt(1));
                    }
                }

                for (Integer bId : bookingIds) {
                    // Delete payments
                    String delPayments = "DELETE FROM payment WHERE booking_id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(delPayments)) {
                        ps.setInt(1, bId);
                        ps.executeUpdate();
                    }
                    // Delete booking packages
                    String delPackages = "DELETE FROM booking_package WHERE booking_id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(delPackages)) {
                        ps.setInt(1, bId);
                        ps.executeUpdate();
                    }
                    // Delete finance records
                    String delFinance = "DELETE FROM company_finance WHERE booking_id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(delFinance)) {
                        ps.setInt(1, bId);
                        ps.executeUpdate();
                    }
                }

                // 3. Delete bookings
                String delBookings = "DELETE FROM booking WHERE customer_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(delBookings)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }

                // 4. Delete events
                String delEvents = "DELETE FROM event WHERE customer_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(delEvents)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }

                // 5. Finally delete customer
                String sqlFinal = "DELETE FROM customer WHERE customer_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sqlFinal)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Error during cascade delete of customer", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);
        }
    }

    public CustomerDTO update(CustomerDTO customerDTO) {
        String sql = "UPDATE customer SET title=?, name=?, username=?, customer_role=?, other_party_name=?, email=?, phone=?, password=? WHERE customer_id=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerDTO.getTitle());
            ps.setString(2, customerDTO.getName());
            ps.setString(3, customerDTO.getUsername());
            ps.setString(4, customerDTO.getCustomerRole());
            ps.setString(5, customerDTO.getOtherPartyName());
            ps.setString(6, customerDTO.getEmail());
            ps.setString(7, customerDTO.getPhone());
            ps.setString(8, customerDTO.getPassword());
            ps.setInt(9, customerDTO.getId());
            ps.executeUpdate();
            return customerDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating customer", e);
        }
    }

    private CustomerDTO mapRowToCustomer(ResultSet rs) throws SQLException {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(rs.getInt("customer_id"));
        dto.setTitle(rs.getString("title"));
        dto.setName(rs.getString("name"));
        dto.setUsername(rs.getString("username"));
        dto.setCustomerRole(rs.getString("customer_role"));
        dto.setOtherPartyName(rs.getString("other_party_name"));
        dto.setEmail(rs.getString("email"));
        dto.setPhone(rs.getString("phone"));
        dto.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        return dto;
    }
}
