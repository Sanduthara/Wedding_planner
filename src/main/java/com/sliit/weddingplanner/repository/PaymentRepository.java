package com.sliit.weddingplanner.repository;

import com.sliit.weddingplanner.db.DBConnection;
import com.sliit.weddingplanner.dto.PaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: PaymentRepository depends on DBConnection
@Repository
public class PaymentRepository {

    private final DBConnection dbConnection;

    @Autowired
    public PaymentRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public PaymentDTO save(PaymentDTO paymentDTO) {
        String sql = "INSERT INTO payment (booking_id, total_amount, amount, due_amount, payment_type, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, paymentDTO.getBookingId());
            ps.setBigDecimal(2, paymentDTO.getTotalAmount());
            ps.setBigDecimal(3, paymentDTO.getAmount());
            ps.setBigDecimal(4, paymentDTO.getDueAmount() != null ? paymentDTO.getDueAmount() : paymentDTO.getTotalAmount().subtract(paymentDTO.getAmount()));
            ps.setString(5, paymentDTO.getPaymentType() != null ? paymentDTO.getPaymentType() : "FULL");
            ps.setString(6, paymentDTO.getStatus() != null ? paymentDTO.getStatus() : "PENDING");

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) paymentDTO.setPaymentId(rs.getInt(1));
            }
            return paymentDTO;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1452 || e.getMessage().contains("foreign key constraint fails")) {
                throw new com.sliit.weddingplanner.exception.ValidationException("Invalid booking ID. The specified booking does not exist.");
            }
            throw new RuntimeException("Error saving payment", e);
        }
    }

    public Optional<PaymentDTO> findById(int id) {
        String sql = "SELECT * FROM payment WHERE payment_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToPayment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding payment by ID", e);
        }
        return Optional.empty();
    }

    public PaymentDTO update(PaymentDTO p) {
        String sql = "UPDATE payment SET booking_id=?, total_amount=?, amount=?, due_amount=?, payment_type=?, status=? WHERE payment_id=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getBookingId());
            ps.setBigDecimal(2, p.getTotalAmount());
            ps.setBigDecimal(3, p.getAmount());
            ps.setBigDecimal(4, p.getDueAmount());
            ps.setString(5, p.getPaymentType());
            ps.setString(6, p.getStatus());
            ps.setInt(7, p.getPaymentId());
            ps.executeUpdate();
            return p;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating payment", e);
        }
    }

    public List<PaymentDTO> findAll() {
        List<PaymentDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM payment";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRowToPayment(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all payments", e);
        }
        return list;
    }

    public List<PaymentDTO> findAllByCustomerId(int customerId) {
        List<PaymentDTO> list = new ArrayList<>();
        String sql = "SELECT p.*, e.event_name FROM payment p " +
                     "JOIN booking b ON p.booking_id = b.booking_id " +
                     "JOIN event e ON b.event_id = e.event_id " +
                     "WHERE b.customer_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PaymentDTO p = mapRowToPayment(rs);
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding payments by customer", e);
        }
        return list;
    }

    public Optional<PaymentDTO> findByBookingId(int bookingId) {
        String sql = "SELECT * FROM payment WHERE booking_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToPayment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding payment by booking", e);
        }
        return Optional.empty();
    }

    public void updateStatus(int paymentId, String status) {
        String sql = "UPDATE payment SET status = ? WHERE payment_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, paymentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating payment status", e);
        }
    }

    private PaymentDTO mapRowToPayment(ResultSet rs) throws SQLException {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(rs.getInt("payment_id"));
        dto.setEventName(hasColumn(rs, "event_name") ? rs.getString("event_name") : null);
        dto.setBookingId(rs.getInt("booking_id"));
        dto.setTotalAmount(rs.getBigDecimal("total_amount"));
        dto.setAmount(rs.getBigDecimal("amount"));
        dto.setDueAmount(rs.getBigDecimal("due_amount"));
        dto.setPaymentType(rs.getString("payment_type"));
        dto.setStatus(rs.getString("status"));
        dto.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
        return dto;
    }

    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equalsIgnoreCase(rsmd.getColumnLabel(x))) return true;
        }
        return false;
    }
}
