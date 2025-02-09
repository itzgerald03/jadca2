package dbaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {
    
    // Create (Insert) a new user
    public int createUser(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int generatedId = -1;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO members (name, email, phone, address, password, role) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
            pstmt = conn.prepareStatement(sql);
            
            // Hash the password before storing
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getAddress());
            pstmt.setString(5, hashedPassword);
            pstmt.setString(6, user.getRole());
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error in createUser: " + e.getMessage());
            throw e;
        } finally {
            closeAll(conn, pstmt, rs);
        }
        return generatedId;
    }
    
    // Read (Retrieve) user by ID
    public User getUserById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM members WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error in getUserById: " + e.getMessage());
            throw e;
        } finally {
            closeAll(conn, pstmt, rs);
        }
        return user;
    }
    
    // Read (Retrieve) user by email
    public User getUserByEmail(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM members WHERE LOWER(email) = LOWER(?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error in getUserByEmail: " + e.getMessage());
            throw e;
        } finally {
            closeAll(conn, pstmt, rs);
        }
        return user;
    }
    
    // Update user information
    public boolean updateUser(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE members SET name = ?, email = ?, phone = ?, address = ?, role = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getAddress());
            pstmt.setString(5, user.getRole());
            pstmt.setInt(6, user.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error in updateUser: " + e.getMessage());
            throw e;
        } finally {
            closeAll(conn, pstmt, null);
        }
        return success;
    }
    
    // Update user's password
    public boolean updatePassword(int userId, String newPassword) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE members SET password = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            pstmt.setString(1, hashedPassword);
            pstmt.setInt(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error in updatePassword: " + e.getMessage());
            throw e;
        } finally {
            closeAll(conn, pstmt, null);
        }
        return success;
    }
    
    // Delete user
    public boolean deleteUser(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "DELETE FROM members WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error in deleteUser: " + e.getMessage());
            throw e;
        } finally {
            closeAll(conn, pstmt, null);
        }
        return success;
    }
    
    // Get all users
    public List<User> getAllUsers() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM members ORDER BY id";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllUsers: " + e.getMessage());
            throw e;
        } finally {
            closeAll(conn, pstmt, rs);
        }
        return users;
    }
    
    // Verify user for login
    public User verifyUser(String email, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM members WHERE LOWER(email) = LOWER(?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (BCrypt.checkpw(password, storedHash)) {
                    user = mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in verifyUser: " + e.getMessage());
            throw e;
        } finally {
            closeAll(conn, pstmt, rs);
        }
        return user;
    }
    
    // Validate phone numbers
    public boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("[\\+]65 [0-9]{8}");
    }

    // Check email uniqueness
    public boolean isEmailUnique(String email) throws SQLException {
        return getUserByEmail(email) == null;
    }
    
    // Search users by name or email
    public List<User> searchUsers(String searchTerm) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM members WHERE LOWER(name) LIKE LOWER(?) OR LOWER(email) LIKE LOWER(?)";
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error in searchUsers: " + e.getMessage());
            throw e;
        } finally {
            closeAll(conn, pstmt, rs);
        }
        return users;
    }
    
    // Helper method to map ResultSet to User object
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setRegistration_date(rs.getTimestamp("registration_date"));
        return user;
    }
    
    // Helper method to close database resources
    private void closeAll(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Error closing database resources: " + e.getMessage());
        }
    }
}