package com.sliit.weddingplanner.repository;

import com.sliit.weddingplanner.db.DBConnection;
import com.sliit.weddingplanner.dto.BookingPackageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookingPackageRepository {

    private final DBConnection dbConnection;

    @Autowired
    public BookingPackageRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public BookingPackageDTO save(BookingPackageDTO dto) {
        String sql = "INSERT INTO booking_package (booking_id, event_package_id, quantity, price_at_booking, vendor_status, notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, dto.getBookingId());
            ps.setInt(2, dto.getEventPackageId());
            ps.setInt(3, dto.getQuantity() != null ? dto.getQuantity() : 1);
            ps.setBigDecimal(4, dto.getPriceAtBooking());
            ps.setString(5, dto.getVendorStatus() != null ? dto.getVendorStatus() : "PENDING");
            ps.setString(6, dto.getNotes());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) dto.setBookingPackageId(rs.getInt(1));
            }
            return dto;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving booking package", e);
        }
    }

    public List<BookingPackageDTO> findAllByBookingId(int bookingId) {
        List<BookingPackageDTO> list = new ArrayList<>();
        String sql = "SELECT bp.*, p.title as package_title, v.name as vendor_name FROM booking_package bp " +
                     "JOIN event_package ep ON bp.event_package_id = ep.event_package_id " +
                     "JOIN package p ON ep.package_id = p.package_id " +
                     "JOIN vendor v ON p.vendor_id = v.vendor_id " +
                     "WHERE bp.booking_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding packages by booking", e);
        }
        return list;
    }

    public int getBookingIdByPackageId(int packageId) {
        String sql = "SELECT booking_id FROM booking_package WHERE booking_package_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting booking ID", e);
        }
        return -1;
    }

    public void updateVendorStatus(int id, String status, String rejectionReason) {
        String sql = "UPDATE booking_package SET vendor_status = ?, rejection_reason = ?, vendor_responded_at = CURRENT_TIMESTAMP WHERE booking_package_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, rejectionReason);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating vendor response", e);
        }
    }

    public void deleteAllByBookingId(int bookingId) {
        String sql = "DELETE FROM booking_package WHERE booking_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting packages for booking", e);
        }
    }

    public List<BookingPackageDTO> findAllByVendorId(int vendorId) {
        List<BookingPackageDTO> list = new ArrayList<>();
        String sql = "SELECT bp.*, p.title as package_title, b.location, b.booking_date as event_date, c.name as customer_name, e.status as event_status " +
                     "FROM booking_package bp " +
                     "JOIN event_package ep ON bp.event_package_id = ep.event_package_id " +
                     "JOIN package p ON ep.package_id = p.package_id " +
                     "JOIN booking b ON bp.booking_id = b.booking_id " +
                     "JOIN customer c ON b.customer_id = c.customer_id " +
                     "JOIN event e ON b.event_id = e.event_id " +
                     "WHERE p.vendor_id = ? AND e.status != 'DELETED'";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vendorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding packages by vendor", e);
        }
        return list;
    }

    private BookingPackageDTO mapRow(ResultSet rs) throws SQLException {
        BookingPackageDTO dto = new BookingPackageDTO();
        dto.setBookingPackageId(rs.getInt("booking_package_id"));
        dto.setBookingId(rs.getInt("booking_id"));
        dto.setEventPackageId(rs.getInt("event_package_id"));
        dto.setQuantity(rs.getInt("quantity"));
        dto.setPriceAtBooking(rs.getBigDecimal("price_at_booking"));
        dto.setPackageTitle(rs.getString("package_title"));
        dto.setVendorName(hasColumn(rs, "vendor_name") ? rs.getString("vendor_name") : null);
        dto.setVendorStatus(rs.getString("vendor_status"));
        dto.setCustomerName(hasColumn(rs, "customer_name") ? rs.getString("customer_name") : null);
        dto.setEventStatus(hasColumn(rs, "event_status") ? rs.getString("event_status") : null);
        dto.setEventDate(hasColumn(rs, "event_date") && rs.getDate("event_date") != null ? rs.getDate("event_date").toLocalDate() : null);
        dto.setLocation(hasColumn(rs, "location") ? rs.getString("location") : null);
        dto.setVendorRespondedAt(rs.getTimestamp("vendor_responded_at") != null ? rs.getTimestamp("vendor_responded_at").toLocalDateTime() : null);
        dto.setRejectionReason(rs.getString("rejection_reason"));
        dto.setNotes(rs.getString("notes"));
        dto.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        return dto;
    }

    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(rsmd.getColumnLabel(x))) {
                return true;
            }
        }
        return false;
    }
}
