package dbaccess;

import java.sql.*;

public class PaymentDAO {
    
    public Payment createPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (booking_id, payment_intent_id, amount, status) VALUES (?, ?, ?, ?) RETURNING id, created_at";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, payment.getBookingId());
            pstmt.setString(2, payment.getPaymentIntentId());
            pstmt.setDouble(3, payment.getAmount());
            pstmt.setString(4, payment.getStatus());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    payment.setId(rs.getInt("id"));
                    payment.setCreatedAt(rs.getTimestamp("created_at"));
                }
            }
        }
        return payment;
    }

    public boolean updatePaymentStatus(String paymentIntentId, String status) throws SQLException {
        String sql = "UPDATE payments SET status = ? WHERE payment_intent_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, paymentIntentId);
            
            return pstmt.executeUpdate() > 0;
        }
    }

    public Payment getPaymentByBookingId(int bookingId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE booking_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPayment(rs);
                }
            }
        }
        return null;
    }

    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getInt("id"));
        payment.setBookingId(rs.getInt("booking_id"));
        payment.setPaymentIntentId(rs.getString("payment_intent_id"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setStatus(rs.getString("status"));
        payment.setCreatedAt(rs.getTimestamp("created_at"));
        return payment;
    }
}