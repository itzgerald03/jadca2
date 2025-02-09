package servlets;

import java.io.*;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import dbaccess.ServiceDAO;
import dbaccess.Service;

@WebServlet("/DeleteServiceServlet")
public class DeleteServiceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServiceDAO serviceDAO;
    
    public void init() throws ServletException {
        serviceDAO = new ServiceDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String serviceId = request.getParameter("id");
        HttpSession session = request.getSession();
        
        try {
            if (serviceId == null || serviceId.trim().isEmpty()) {
                session.setAttribute("error", "Invalid service ID.");
                response.sendRedirect("CA1/adminPage.jsp");
                return;
            }
            
            // Get service to find image path
            Service service = serviceDAO.getServiceById(Integer.parseInt(serviceId));
            if (service != null && service.getImageUrl() != null) {
                // Delete the image file
                String imagePath = getServletContext().getRealPath("/") + "CA1" + service.getImageUrl();
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    imageFile.delete();
                }
            }
            
            if (serviceDAO.deleteService(Integer.parseInt(serviceId))) {
                session.setAttribute("success", "Service deleted successfully!");
            } else {
                session.setAttribute("error", "Failed to delete service.");
            }
            
        } catch (SQLException e) {
            System.out.println("Database error deleting service: " + e.getMessage());
            session.setAttribute("error", "Database error occurred. Please try again later.");
        } catch (Exception e) {
            System.out.println("Unexpected error deleting service: " + e.getMessage());
            session.setAttribute("error", "An unexpected error occurred. Please try again later.");
        }
        
        response.sendRedirect("CA1/adminPage.jsp");
    }
}