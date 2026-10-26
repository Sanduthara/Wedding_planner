package com.sliit.weddingplanner.repository;

import com.sliit.weddingplanner.db.DBConnection;
import com.sliit.weddingplanner.dto.BookingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: BookingRepository depends on DBConnection
@Repository
public class BookingRepository {

    private final DBConnection dbConnection;

    @Autowired
    public BookingRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public BookingDTO save(BookingDTO bookingDTO) {
        String sql = "INSERT INTO booking (event_id, customer_id, booking_date, location, status, total_cost, payment_type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (bookingDTO.getEventId() != null) ps.setInt(1, bookingDTO.getEventId()); else ps.setNull(1, Types.INTEGER);
            ps.setInt(2, bookingDTO.getCustomerId());
            ps.setDate(3, Date.valueOf(bookingDTO.getBookingDate()));
            ps.setString(4, bookingDTO.getLocation());
            ps.setString(5, bookingDTO.getStatus() != null ? bookingDTO.getStatus() : "PENDING");
            ps.setDouble(6, bookingDTO.getTotalCost() != null ? bookingDTO.getTotalCost() : 0.0);
            ps.setString(7, bookingDTO.getPaymentType() != null ? bookingDTO.getPaymentType() : "FULL");

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) bookingDTO.setBookingId(rs.getInt(1));
            }

            return bookingDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving booking", e);
        }
    }

    public Optional<BookingDTO> findById(int id) {
        String sql = "SELECT * FROM booking WHERE booking_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToBooking(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding booking", e);
        }
        return Optional.empty();
    }

    public Optional<BookingDTO> findByEventId(int eventId) {
        String sql = "SELECT * FROM booking WHERE event_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToBooking(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding booking by event", e);
        }
        return Optional.empty();
    }

    public List<BookingDTO> findAll() {
        List<BookingDTO> list = new ArrayList<>();
        String sql = "SELECT b.*, c.name as customer_name, e.event_name, e.status as event_status " +
                     "FROM booking b " +
                     "LEFT JOIN customer c ON b.customer_id = c.customer_id " +
                     "LEFT JOIN event e ON b.event_id = e.event_id";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRowToBooking(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all bookings", e);
        }
        return list;
    }

    public List<BookingDTO> findAllByCustomerId(int customerId) {
        List<BookingDTO> list = new ArrayList<>();
        String sql = "SELECT b.*, e.event_name, e.status as event_status, p.status as payment_status " +
                     "FROM booking b " +
                     "JOIN event e ON b.event_id = e.event_id " +
                     "LEFT JOIN payment p ON b.booking_id = p.booking_id " +
                     "WHERE b.customer_id = ? AND e.status != 'DELETED'";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRowToBooking(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding bookings by customer", e);
        }
        return list;
    }

    public void updateStatus(int bookingId, String status) {
        String sql = "UPDATE booking SET status = ? WHERE booking_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating booking status", e);
        }
    }

    public BookingDTO update(BookingDTO bookingDTO) {
        String sql = "UPDATE booking SET event_id=?, customer_id=?, booking_date=?, location=?, status=?, total_cost=?, payment_type=? WHERE booking_id=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (bookingDTO.getEventId() != null) ps.setInt(1, bookingDTO.getEventId()); else ps.setNull(1, Types.INTEGER);
            ps.setInt(2, bookingDTO.getCustomerId());
            ps.setDate(3, Date.valueOf(bookingDTO.getBookingDate()));
            ps.setString(4, bookingDTO.getLocation());
            ps.setString(5, bookingDTO.getStatus());
            ps.setDouble(6, bookingDTO.getTotalCost() != null ? bookingDTO.getTotalCost() : 0.0);
            ps.setString(7, bookingDTO.getPaymentType() != null ? bookingDTO.getPaymentType() : "FULL");
            ps.setInt(8, bookingDTO.getBookingId());
            ps.executeUpdate();

            return bookingDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating booking", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM booking WHERE booking_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting booking", e);
        }
    }

    private BookingDTO mapRowToBooking(ResultSet rs) throws SQLException {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(rs.getInt("booking_id"));
        dto.setEventId(rs.getObject("event_id") != null ? rs.getInt("event_id") : null);
        dto.setEventName(hasColumn(rs, "event_name") ? rs.getString("event_name") : null);
        dto.setEventStatus(hasColumn(rs, "event_status") ? rs.getString("event_status") : null);
        dto.setCustomerId(rs.getInt("customer_id"));
        dto.setCustomerName(hasColumn(rs, "customer_name") ? rs.getString("customer_name") : null);
        dto.setBookingDate(rs.getDate("booking_date").toLocalDate());
        dto.setLocation(rs.getString("location"));
        dto.setStatus(rs.getString("status"));
        dto.setPaymentStatus(hasColumn(rs, "payment_status") ? rs.getString("payment_status") : null);
        dto.setTotalCost(rs.getDouble("total_cost"));
        dto.setPaymentType(hasColumn(rs, "payment_type") ? rs.getString("payment_type") : "FULL");
        dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
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
