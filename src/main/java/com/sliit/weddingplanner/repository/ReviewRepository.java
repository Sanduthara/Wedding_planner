package com.sliit.weddingplanner.repository;

import com.sliit.weddingplanner.db.DBConnection;
import com.sliit.weddingplanner.dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: ReviewRepository depends on DBConnection
@Repository
public class ReviewRepository {

    private final DBConnection dbConnection;

    @Autowired
    public ReviewRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public ReviewDTO save(ReviewDTO reviewDTO) {
        String sql = "INSERT INTO review (event_id, customer_id, package_id, package_rating, package_comment) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, reviewDTO.getEventId());
            if (reviewDTO.getCustomerId() != null) ps.setInt(2, reviewDTO.getCustomerId()); else ps.setNull(2, Types.INTEGER);
            if (reviewDTO.getPackageId() != null) ps.setInt(3, reviewDTO.getPackageId()); else ps.setNull(3, Types.INTEGER);
            if (reviewDTO.getPackageRating() != null) ps.setInt(4, reviewDTO.getPackageRating()); else ps.setNull(4, Types.INTEGER);
            ps.setString(5, reviewDTO.getPackageComment());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) reviewDTO.setReviewId(rs.getInt(1));
            }
            return reviewDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving review", e);
        }
    }

    public Optional<ReviewDTO> findById(int id) {
        String sql = "SELECT * FROM review WHERE review_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToReview(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding review by ID", e);
        }
        return Optional.empty();
    }

    public List<ReviewDTO> findAllByVendorId(int vendorId) {
        List<ReviewDTO> list = new ArrayList<>();
        String sql = "SELECT r.*, c.name as customer_name FROM review r " +
                     "JOIN customer c ON r.customer_id = c.customer_id " +
                     "JOIN package p ON r.package_id = p.package_id " +
                     "WHERE p.vendor_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vendorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRowToReview(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding reviews by vendor", e);
        }
        return list;
    }

    public List<ReviewDTO> findAllByPackageId(int packageId) {
        List<ReviewDTO> list = new ArrayList<>();
        String sql = "SELECT r.*, c.name as customer_name FROM review r " +
                     "JOIN customer c ON r.customer_id = c.customer_id " +
                     "WHERE r.package_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRowToReview(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding reviews by package", e);
        }
        return list;
    }

    public List<ReviewDTO> findAll() {
        List<ReviewDTO> list = new ArrayList<>();
        String sql = "SELECT r.*, c.name as customer_name FROM review r " +
                     "JOIN customer c ON r.customer_id = c.customer_id";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRowToReview(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all reviews", e);
        }
        return list;
    }

    public void delete(int id) {
        String sql = "DELETE FROM review WHERE review_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting review", e);
        }
    }

    private ReviewDTO mapRowToReview(ResultSet rs) throws SQLException {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(rs.getInt("review_id"));
        dto.setEventId(rs.getInt("event_id"));
        dto.setCustomerId(rs.getObject("customer_id") != null ? rs.getInt("customer_id") : null);
        dto.setPackageId(rs.getObject("package_id") != null ? rs.getInt("package_id") : null);
        dto.setPackageRating(rs.getObject("package_rating") != null ? rs.getInt("package_rating") : null);
        dto.setPackageComment(rs.getString("package_comment"));
        
        // Handle optional customer_name from JOIN
        try {
            dto.setCustomerName(rs.getString("customer_name"));
        } catch (SQLException e) {
            // Column might not exist in some simple SELECTs
        }

        // Defensive timestamp reading
        Timestamp created = null;
        try {
            created = rs.getTimestamp("created_at");
        } catch (SQLException e) {
            // Column might not exist
        }
        if (created != null) dto.setCreatedAt(created.toLocalDateTime());

        Timestamp reviewed = null;
        try {
            reviewed = rs.getTimestamp("reviewed_at");
        } catch (SQLException e) {
            // Column might not exist
        }
        if (reviewed != null) dto.setReviewedAt(reviewed.toLocalDateTime());

        return dto;
    }
}
