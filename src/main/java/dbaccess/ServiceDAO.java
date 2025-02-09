package dbaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    
    public List<Service> getAllServices() throws SQLException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT s.*, c.category_name FROM services s " +
                    "JOIN service_categories c ON s.category_id = c.id " +
                    "ORDER BY s.id";
                    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        }
        return services;
    }
    
    public Service getServiceById(int id) throws SQLException {
        String sql = "SELECT s.*, c.category_name FROM services s " +
                    "JOIN service_categories c ON s.category_id = c.id " +
                    "WHERE s.id = ?";
                    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToService(rs);
                }
            }
        }
        return null;
    }
    
    public int createService(Service service, String uploadPath) throws SQLException {
        String sql = "INSERT INTO services (service_name, description, price, category_id, image_url) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING id";
                    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, service.getServiceName());
            pstmt.setString(2, service.getDescription());
            pstmt.setDouble(3, service.getPrice());
            pstmt.setInt(4, service.getCategoryId());
            pstmt.setString(5, service.getImageUrl());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }
    
    public boolean updateService(Service service) throws SQLException {
        String sql = "UPDATE services SET service_name = ?, description = ?, " +
                    "price = ?, category_id = ?, image_url = ? WHERE id = ?";
                    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, service.getServiceName());
            pstmt.setString(2, service.getDescription());
            pstmt.setDouble(3, service.getPrice());
            pstmt.setInt(4, service.getCategoryId());
            pstmt.setString(5, service.getImageUrl());
            pstmt.setInt(6, service.getId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean deleteService(int id) throws SQLException {
        String sql = "DELETE FROM services WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public List<Service> getServicesByCategory(int categoryId) throws SQLException {
        String sql = "SELECT s.*, c.category_name FROM services s " +
                    "JOIN service_categories c ON s.category_id = c.id " +
                    "WHERE s.category_id = ? " +
                    "ORDER BY s.id";
                    
        List<Service> services = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, categoryId);
            System.out.println("ServiceDAO: Executing query for category ID: " + categoryId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Service service = mapResultSetToService(rs);
                    System.out.println("ServiceDAO: Found service: " + service.getServiceName());
                    services.add(service);
                }
            }
        }
        System.out.println("ServiceDAO: Returning " + services.size() + " services for category " + categoryId);
        return services;
    }
    
    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        Service service = new Service();
        service.setId(rs.getInt("id"));
        service.setCategoryId(rs.getInt("category_id"));
        service.setCategoryName(rs.getString("category_name"));
        service.setServiceName(rs.getString("service_name"));
        service.setDescription(rs.getString("description"));
        service.setPrice(rs.getDouble("price"));
        service.setImageUrl(rs.getString("image_url"));
        return service;
    }
}