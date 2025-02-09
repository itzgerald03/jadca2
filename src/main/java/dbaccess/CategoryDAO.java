package dbaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    
    // Create (Insert) a new category
    public int createCategory(Category category) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int generatedId = -1;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO service_categories (category_name) VALUES (?) RETURNING id";
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, category.getCategoryName());
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error in createCategory: " + e.getMessage());
            throw e;
        } finally {
            closeAll(conn, pstmt, rs);
        }
        return generatedId;
    }
    
    // Read (Retrieve) category by ID
    public Category getCategoryById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Category category = null;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM service_categories WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                category = mapResultSetToCategory(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error in getCategoryById: " + e.getMessage());
            throw e;
        } finally {
            closeAll(conn, pstmt, rs);
        }
        return category;
    }
    
    // Get all categories
    public List<Category> getAllCategories() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Category> categories = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM service_categories ORDER BY id";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Category category = mapResultSetToCategory(rs);
                System.out.println("Retrieved Category: " + category.getCategoryName()); 
                categories.add(category);
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllCategories: " + e.getMessage());
            throw e;
        } finally {
            closeAll(conn, pstmt, rs);
        }
        return categories;
    }
    
    // Update category
    public boolean updateCategory(Category category) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE service_categories SET category_name = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, category.getCategoryName());
            pstmt.setInt(2, category.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error in updateCategory: " + e.getMessage());
            throw e;
        } finally {
            closeAll(conn, pstmt, null);
        }
        return success;
    }
    
    // Delete category
    public boolean deleteCategory(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "DELETE FROM service_categories WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error in deleteCategory: " + e.getMessage());
            throw e;
        } finally {
            closeAll(conn, pstmt, null);
        }
        return success;
    }
    
    // Search categories by name
    public List<Category> searchCategories(String searchTerm) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Category> categories = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM service_categories WHERE LOWER(category_name) LIKE LOWER(?)";
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error in searchCategories: " + e.getMessage());
            throw e;
        } finally {
            closeAll(conn, pstmt, rs);
        }
        return categories;
    }

    // Check if category name exists
    public boolean isCategoryNameUnique(String categoryName) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM service_categories WHERE LOWER(category_name) = LOWER(?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, categoryName);
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            System.out.println("Error in isCategoryNameUnique: " + e.getMessage());
            throw e;
        } finally {
            closeAll(conn, pstmt, rs);
        }
        return true;
    }
    
    // Helper method to map ResultSet to Category object
    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt("id"));
        category.setCategoryName(rs.getString("category_name"));
        return category;
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