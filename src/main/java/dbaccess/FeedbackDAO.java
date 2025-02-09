package dbaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {
    
    public List<Feedback> getAllFeedback() throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT f.*, m.name as member_name, s.service_name " +
                    "FROM feedback f " +
                    "JOIN members m ON f.member_id = m.id " +
                    "JOIN bookings b ON f.booking_id = b.id " +
                    "JOIN booking_details bd ON b.id = bd.booking_id " +
                    "JOIN services s ON bd.service_id = s.id " +
                    "ORDER BY f.feedback_date DESC";
                    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                feedbackList.add(mapResultSetToFeedback(rs));
            }
        }
        return feedbackList;
    }
    
    public Feedback getFeedbackById(int id) throws SQLException {
        String sql = "SELECT f.*, m.name as member_name, s.service_name " +
                    "FROM feedback f " +
                    "JOIN members m ON f.member_id = m.id " +
                    "JOIN bookings b ON f.booking_id = b.id " +
                    "JOIN booking_details bd ON b.id = bd.booking_id " +
                    "JOIN services s ON bd.service_id = s.id " +
                    "WHERE f.id = ?";
                    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFeedback(rs);
                }
            }
        }
        return null;
    }
    
    public boolean createFeedback(Feedback feedback) throws SQLException {
        String sql = "INSERT INTO feedback (booking_id, member_id, rating, comments) " +
                    "VALUES (?, ?, ?, ?)";
                    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, feedback.getBookingId());
            pstmt.setInt(2, feedback.getMemberId());
            pstmt.setInt(3, feedback.getRating());
            pstmt.setString(4, feedback.getComments());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean updateFeedback(Feedback feedback) throws SQLException {
        String sql = "UPDATE feedback SET rating = ?, comments = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, feedback.getRating());
            pstmt.setString(2, feedback.getComments());
            pstmt.setInt(3, feedback.getId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean deleteFeedback(int id) throws SQLException {
        String sql = "DELETE FROM feedback WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    private Feedback mapResultSetToFeedback(ResultSet rs) throws SQLException {
        Feedback feedback = new Feedback();
        feedback.setId(rs.getInt("id"));
        feedback.setBookingId(rs.getInt("booking_id"));
        feedback.setMemberId(rs.getInt("member_id"));
        feedback.setMemberName(rs.getString("member_name"));
        feedback.setServiceName(rs.getString("service_name"));
        feedback.setRating(rs.getInt("rating"));
        feedback.setComments(rs.getString("comments"));
        feedback.setFeedbackDate(rs.getTimestamp("feedback_date"));
        return feedback;
    }
}