package com.sliit.weddingplanner.repository;

import com.sliit.weddingplanner.db.DBConnection;
import com.sliit.weddingplanner.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// OOP: Encapsulation
// OOP: Dependency Injection
@Repository
public class CategoryRepository {

    private final DBConnection dbConnection;

    @Autowired
    public CategoryRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public CategoryDTO save(CategoryDTO categoryDTO) {
        String sql = "INSERT INTO category (category_name, description) VALUES (?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, categoryDTO.getCategoryName());
            ps.setString(2, categoryDTO.getDescription());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) categoryDTO.setCategoryId(rs.getInt(1));
            }
            return categoryDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving category", e);
        }
    }

    public Optional<CategoryDTO> findById(int id) {
        String sql = "SELECT * FROM category WHERE category_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToCategory(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding category by ID", e);
        }
        return Optional.empty();
    }

    public List<CategoryDTO> findAll() {
        List<CategoryDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM category";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRowToCategory(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all categories", e);
        }
        return list;
    }

    public CategoryDTO update(CategoryDTO categoryDTO) {
        String sql = "UPDATE category SET category_name=?, description=? WHERE category_id=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoryDTO.getCategoryName());
            ps.setString(2, categoryDTO.getDescription());
            ps.setInt(3, categoryDTO.getCategoryId());
            ps.executeUpdate();
            return categoryDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating category", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM category WHERE category_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451 || e.getMessage().contains("foreign key constraint fails")) {
                throw new com.sliit.weddingplanner.exception.ValidationException("Cannot delete category because it is currently assigned to one or more packages.");
            }
            throw new RuntimeException("Error deleting category", e);
        }
    }

    private CategoryDTO mapRowToCategory(ResultSet rs) throws SQLException {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(rs.getInt("category_id"));
        dto.setCategoryName(rs.getString("category_name"));
        dto.setDescription(rs.getString("description"));
        dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return dto;
    }
}
