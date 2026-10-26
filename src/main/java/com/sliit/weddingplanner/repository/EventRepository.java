package com.sliit.weddingplanner.repository;

import com.sliit.weddingplanner.db.DBConnection;
import com.sliit.weddingplanner.dto.EventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.Types;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: EventRepository depends on DBConnection
@Repository
public class EventRepository {

    private final DBConnection dbConnection;

    @Autowired
    public EventRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public EventDTO save(EventDTO eventDTO) {
        String sql = "INSERT INTO event (customer_id, event_name, event_type, event_date, location, description, status, event_rating, event_review) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, eventDTO.getCustomerId());
            ps.setString(2, eventDTO.getEventName());
            ps.setString(3, eventDTO.getEventType() != null ? eventDTO.getEventType() : "Wedding");
            ps.setDate(4, eventDTO.getEventDate() != null ? Date.valueOf(eventDTO.getEventDate()) : null);
            ps.setString(5, eventDTO.getLocation());
            ps.setString(6, eventDTO.getDescription());
            ps.setString(7, eventDTO.getStatus() != null ? eventDTO.getStatus() : "PLANNING");
            if (eventDTO.getEventRating() != null && eventDTO.getEventRating() != 0) {
                ps.setInt(8, eventDTO.getEventRating());
            } else {
                ps.setNull(8, Types.INTEGER);
            }
            ps.setString(9, eventDTO.getEventReview() != null ? eventDTO.getEventReview() : "");

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    eventDTO.setEventId(rs.getInt(1));
                }
            }
            return eventDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving event", e);
        }
    }

    public Optional<EventDTO> findById(int id) {
        String sql = "SELECT * FROM event WHERE event_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToEvent(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding event by ID", e);
        }
        return Optional.empty();
    }

    public List<EventDTO> findAll() {
        List<EventDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM event";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRowToEvent(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all events", e);
        }
        return list;
    }

    public List<EventDTO> findAllByCustomerId(int customerId) {
        List<EventDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM event WHERE customer_id = ? AND status != 'DELETED'";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRowToEvent(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding events by customer", e);
        }
        return list;
    }

    public EventDTO update(EventDTO eventDTO) {
        String sql = "UPDATE event SET event_name=?, event_type=?, event_date=?, location=?, description=?, status=?, event_rating=?, event_review=? WHERE event_id=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, eventDTO.getEventName());
            ps.setString(2, eventDTO.getEventType());
            ps.setDate(3, eventDTO.getEventDate() != null ? Date.valueOf(eventDTO.getEventDate()) : null);
            ps.setString(4, eventDTO.getLocation());
            ps.setString(5, eventDTO.getDescription());
            ps.setString(6, eventDTO.getStatus());
            if (eventDTO.getEventRating() != null && eventDTO.getEventRating() != 0) {
                ps.setInt(7, eventDTO.getEventRating());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.setString(8, eventDTO.getEventReview());
            ps.setInt(9, eventDTO.getEventId());
            ps.executeUpdate();
            return eventDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating event", e);
        }
    }

    public List<EventDTO> findPendingReviewsByCustomerId(int customerId) {
        List<EventDTO> list = new ArrayList<>();
        // Query to find events that passed, have no rating, and associated payment is PAID
        String sql = "SELECT e.* FROM event e " +
                     "JOIN booking b ON e.event_id = b.event_id " +
                     "JOIN payment p ON b.booking_id = p.booking_id " +
                     "WHERE e.customer_id = ? " +
                     "AND e.event_date < CURDATE() " +
                     "AND (e.event_rating IS NULL OR e.event_rating = 0) " +
                     "AND p.status = 'PAID' " +
                     "AND e.status != 'DELETED'";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRowToEvent(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding pending reviews", e);
        }
        return list;
    }

    public void submitReview(int eventId, int rating, String review) {
        String sql = "UPDATE event SET event_rating = ?, event_review = ?, reviewed_at = CURRENT_TIMESTAMP WHERE event_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rating);
            ps.setString(2, review);
            ps.setInt(3, eventId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error submitting event review", e);
        }
    }

    public void delete(int id) {
        String sql = "UPDATE event SET status = 'DELETED' WHERE event_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error soft deleting event", e);
        }
    }

    private EventDTO mapRowToEvent(ResultSet rs) throws SQLException {
        EventDTO dto = new EventDTO();
        dto.setEventId(rs.getInt("event_id"));
        dto.setCustomerId(rs.getInt("customer_id"));
        dto.setEventName(rs.getString("event_name"));
        dto.setEventType(rs.getString("event_type"));
        dto.setEventDate(rs.getDate("event_date") != null ? rs.getDate("event_date").toLocalDate() : null);
        dto.setLocation(rs.getString("location"));
        dto.setDescription(rs.getString("description"));
        dto.setStatus(rs.getString("status"));
        dto.setEventRating(rs.getInt("event_rating"));
        dto.setEventReview(rs.getString("event_review"));
        dto.setReviewedAt(rs.getTimestamp("reviewed_at") != null ? rs.getTimestamp("reviewed_at").toLocalDateTime() : null);
        dto.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        return dto;
    }
}
