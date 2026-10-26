package com.sliit.weddingplanner.repository;

import com.sliit.weddingplanner.db.DBConnection;
import com.sliit.weddingplanner.dto.EventPackageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EventPackageRepository {

    private final DBConnection dbConnection;

    @Autowired
    public EventPackageRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public EventPackageDTO save(EventPackageDTO dto) {
        String sql = "INSERT INTO event_package (event_id, package_id, quantity, notes) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, dto.getEventId());
            ps.setInt(2, dto.getPackageId());
            ps.setInt(3, dto.getQuantity() != null ? dto.getQuantity() : 1);
            ps.setString(4, dto.getNotes());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) dto.setEventPackageId(rs.getInt(1));
            }
            return dto;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving event package", e);
        }
    }

    public List<EventPackageDTO> findAllByEventId(int eventId) {
        List<EventPackageDTO> list = new ArrayList<>();
        String sql = "SELECT ep.*, p.title as package_title, p.price as package_price, c.category_name " +
                     "FROM event_package ep " +
                     "JOIN package p ON ep.package_id = p.package_id " +
                     "JOIN category c ON p.category_id = c.category_id " +
                     "WHERE ep.event_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding packages by event", e);
        }
        return list;
    }

    public void delete(int id) {
        String sql = "DELETE FROM event_package WHERE event_package_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting event package", e);
        }
    }

    private EventPackageDTO mapRow(ResultSet rs) throws SQLException {
        EventPackageDTO dto = new EventPackageDTO();
        dto.setEventPackageId(rs.getInt("event_package_id"));
        dto.setEventId(rs.getInt("event_id"));
        dto.setPackageId(rs.getInt("package_id"));
        dto.setPackageTitle(rs.getString("package_title"));
        dto.setCategoryName(rs.getString("category_name"));
        dto.setPackagePrice(rs.getBigDecimal("package_price"));
        dto.setQuantity(rs.getInt("quantity"));
        dto.setNotes(rs.getString("notes"));
        dto.setAddedAt(rs.getTimestamp("added_at").toLocalDateTime());
        return dto;
    }
}
