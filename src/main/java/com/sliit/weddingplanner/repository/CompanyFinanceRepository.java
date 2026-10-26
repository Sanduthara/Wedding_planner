package com.sliit.weddingplanner.repository;

import com.sliit.weddingplanner.db.DBConnection;
import com.sliit.weddingplanner.dto.CompanyFinanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: CompanyFinanceRepository depends on DBConnection
@Repository
public class CompanyFinanceRepository {

    private final DBConnection dbConnection;

    @Autowired
    public CompanyFinanceRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public CompanyFinanceDTO save(CompanyFinanceDTO financeDTO) {
        String sql = "INSERT INTO company_finance (type, booking_id, payment_id, amount, description, payment_method) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, financeDTO.getType());
            if (financeDTO.getBookingId() != null) ps.setInt(2, financeDTO.getBookingId()); else ps.setNull(2, Types.INTEGER);
            if (financeDTO.getPaymentId() != null) ps.setInt(3, financeDTO.getPaymentId()); else ps.setNull(3, Types.INTEGER);
            ps.setBigDecimal(4, financeDTO.getAmount());
            ps.setString(5, financeDTO.getDescription());
            ps.setString(6, financeDTO.getPaymentMethod());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) financeDTO.setFinanceId(rs.getInt(1));
            }
            return financeDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving finance record", e);
        }
    }

    public Optional<CompanyFinanceDTO> findById(int id) {
        String sql = "SELECT * FROM company_finance WHERE finance_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToFinance(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding finance record", e);
        }
        return Optional.empty();
    }

    public List<CompanyFinanceDTO> findAll() {
        List<CompanyFinanceDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM company_finance ORDER BY transaction_date DESC";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRowToFinance(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all finance records", e);
        }
        return list;
    }

    private CompanyFinanceDTO mapRowToFinance(ResultSet rs) throws SQLException {
        CompanyFinanceDTO dto = new CompanyFinanceDTO();
        dto.setFinanceId(rs.getInt("finance_id"));
        dto.setType(rs.getString("type"));
        dto.setBookingId(rs.getObject("booking_id") != null ? rs.getInt("booking_id") : null);
        dto.setPaymentId(rs.getObject("payment_id") != null ? rs.getInt("payment_id") : null);
        dto.setAmount(rs.getBigDecimal("amount"));
        dto.setDescription(rs.getString("description"));
        dto.setPaymentMethod(rs.getString("payment_method"));
        dto.setTransactionDate(rs.getTimestamp("transaction_date").toLocalDateTime());
        dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return dto;
    }
}
