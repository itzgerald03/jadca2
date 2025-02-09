// BookingDAO.java
package dbaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
	
    public Booking getBookingById(int id) throws SQLException {
        Booking booking = null;
        String sql = "SELECT b.*, m.name as member_name, m.address FROM bookings b " +
                    "JOIN members m ON b.member_id = m.id WHERE b.id = ?";
                    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    booking = mapResultSetToBooking(rs);
                    booking.setBookingDetails(getBookingDetails(booking.getId()));
                }
            }
        }
        return booking;
    }
    
    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, m.name as member_name, m.address FROM bookings b " +
                    "JOIN members m ON b.member_id = m.id ORDER BY b.appointment_date DESC";
                    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = mapResultSetToBooking(rs);
                    booking.setBookingDetails(getBookingDetails(booking.getId()));
                    bookings.add(booking);
                }
            }
        }
        return bookings;
    }
    
    private List<BookingDetail> getBookingDetails(int bookingId) throws SQLException {
        List<BookingDetail> details = new ArrayList<>();
        String sql = "SELECT bd.*, s.service_name FROM booking_details bd " +
                    "JOIN services s ON bd.service_id = s.id WHERE bd.booking_id = ?";
                    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    details.add(mapResultSetToBookingDetail(rs));
                }
            }
        }
        return details;
    }
    
    public boolean updateBooking(Booking booking) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Update main booking
            String sql = "UPDATE bookings SET appointment_date = ?, special_requests = ?, " +
                        "status = ? WHERE id = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setTimestamp(1, booking.getAppointmentDate());
                pstmt.setString(2, booking.getSpecialRequests());
                pstmt.setString(3, booking.getStatus());
                pstmt.setInt(4, booking.getId());
                pstmt.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    public boolean deleteBooking(int id) throws SQLException {
        String sql = "DELETE FROM bookings WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setMemberId(rs.getInt("member_id"));
        booking.setMemberName(rs.getString("member_name"));
        booking.setBookingDate(rs.getTimestamp("booking_date"));
        booking.setAppointmentDate(rs.getTimestamp("appointment_date"));
        booking.setSpecialRequests(rs.getString("special_requests"));
        booking.setStatus(rs.getString("status"));
        booking.setAddress(rs.getString("address"));
        return booking;
    }
    
    private BookingDetail mapResultSetToBookingDetail(ResultSet rs) throws SQLException {
        BookingDetail detail = new BookingDetail();
        detail.setId(rs.getInt("id"));
        detail.setBookingId(rs.getInt("booking_id"));
        detail.setServiceId(rs.getInt("service_id"));
        detail.setServiceName(rs.getString("service_name"));
        detail.setQuantity(rs.getInt("quantity"));
        detail.setTotalPrice(rs.getDouble("total_price"));
        return detail;
    }
    

    public List<Booking> getBookingsByMemberId(int memberId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, m.name as member_name, m.address FROM bookings b " +
                     "JOIN members m ON b.member_id = m.id " +
                     "WHERE b.member_id = ? " +
                     "ORDER BY b.appointment_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = mapResultSetToBooking(rs);
                    booking.setBookingDetails(getBookingDetails(booking.getId()));
                    bookings.add(booking);
                }
            }
        }
        return bookings;
    }
    

    public List<Booking> getCompletedBookingsForFeedback(int memberId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, m.name as member_name, m.address " +
                     "FROM bookings b " +
                     "JOIN members m ON b.member_id = m.id " +
                     "WHERE b.member_id = ? AND b.status = 'Completed' " +
                     "AND NOT EXISTS (SELECT 1 FROM feedback f WHERE f.booking_id = b.id) " +
                     "ORDER BY b.appointment_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = mapResultSetToBooking(rs);
                    booking.setBookingDetails(getBookingDetails(booking.getId()));
                    bookings.add(booking);
                }
            }
        }
        return bookings;
    }


    
    public Booking createBooking(Booking booking) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Insert main booking
            String bookingSql = "INSERT INTO bookings (member_id, booking_date, appointment_date, special_requests, status) " +
                              "VALUES (?, ?, ?, ?, ?) RETURNING id";
            
            try (PreparedStatement pstmt = conn.prepareStatement(bookingSql)) {
                pstmt.setInt(1, booking.getMemberId());
                pstmt.setTimestamp(2, booking.getBookingDate());
                pstmt.setTimestamp(3, booking.getAppointmentDate());
                pstmt.setString(4, booking.getSpecialRequests());
                pstmt.setString(5, booking.getStatus());
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        booking.setId(rs.getInt(1));
                    }
                }
            }
            
            // Insert booking details
            String detailSql = "INSERT INTO booking_details (booking_id, service_id, quantity, total_price) " +
                              "VALUES (?, ?, ?, ?)";
                              
            try (PreparedStatement pstmt = conn.prepareStatement(detailSql)) {
                for (BookingDetail detail : booking.getBookingDetails()) {
                    pstmt.setInt(1, booking.getId());
                    pstmt.setInt(2, detail.getServiceId());
                    pstmt.setInt(3, detail.getQuantity());
                    pstmt.setDouble(4, detail.getTotalPrice());
                    pstmt.executeUpdate();
                }
            }
            
            conn.commit();
            return booking;
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    public List<CustomerStats> getTopCustomersByValue(int limit) throws SQLException {
        List<CustomerStats> topCustomers = new ArrayList<>();
        String sql = "SELECT m.id AS customer_id, m.name AS customer_name, COUNT(b.id) AS total_bookings, "
                   + "SUM(bd.total_price) AS total_booking_value "
                   + "FROM members m "
                   + "JOIN bookings b ON m.id = b.member_id "
                   + "JOIN booking_details bd ON b.id = bd.booking_id "
                   + "GROUP BY m.id, m.name "
                   + "ORDER BY total_booking_value DESC "
                   + "LIMIT ?";

        System.out.println("Executing SQL Query: " + sql);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            System.out.println("SQL Parameter Limit: " + limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CustomerStats customer = new CustomerStats();
                    customer.setCustomerId(rs.getInt("customer_id"));
                    customer.setCustomerName(rs.getString("customer_name"));
                    customer.setTotalBookings(rs.getInt("total_bookings"));
                    customer.setTotalBookingValue(rs.getDouble("total_booking_value"));
                    topCustomers.add(customer);
                }
            }
        }
        System.out.println("Fetched Top Customers: " + topCustomers);
        return topCustomers;
    }
}
