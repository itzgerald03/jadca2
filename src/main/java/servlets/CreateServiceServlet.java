package servlets;

import java.io.*;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;
import dbaccess.ServiceDAO;
import dbaccess.Service;

@WebServlet("/CreateServiceServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,    // 1 MB
    maxFileSize = 1024 * 1024 * 10,     // 10 MB
    maxRequestSize = 1024 * 1024 * 15    // 15 MB
)
public class CreateServiceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServiceDAO serviceDAO;
    
    public void init() throws ServletException {
        serviceDAO = new ServiceDAO();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Get and validate form data
            String serviceName = request.getParameter("serviceName");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            String categoryIdStr = request.getParameter("categoryId");
            
            // Basic validation
            if (serviceName == null || serviceName.trim().isEmpty()) {
                session.setAttribute("error", "Service name cannot be empty.");
                response.sendRedirect("CA1/adminPage.jsp");
                return;
            }
            
            if (description == null || description.trim().isEmpty()) {
                session.setAttribute("error", "Description cannot be empty.");
                response.sendRedirect("CA1/adminPage.jsp");
                return;
            }
            
            // Parse and validate numeric values
            double price;
            int categoryId;
            try {
                price = Double.parseDouble(priceStr);
                categoryId = Integer.parseInt(categoryIdStr);
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Invalid price or category format.");
                response.sendRedirect("CA1/adminPage.jsp");
                return;
            }
            
            // Handle image upload
            Part filePart = request.getPart("serviceImage");
            if (filePart == null || filePart.getSize() == 0) {
                session.setAttribute("error", "Service image is required.");
                response.sendRedirect("CA1/adminPage.jsp");
                return;
            }
            
            // Generate unique filename
            String fileName = getSubmittedFileName(filePart);
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            String uploadPath = getServletContext().getRealPath("/") + "CA1/images/";
            String dbImagePath = "/images/" + uniqueFileName;
            
            // Create upload directory if it doesn't exist
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            
            // Save the file
            filePart.write(uploadPath + uniqueFileName);
            
            // Create service object
            Service service = new Service();
            service.setServiceName(serviceName);
            service.setDescription(description);
            service.setPrice(price);
            service.setCategoryId(categoryId);
            service.setImageUrl(dbImagePath);
            
            // Save to database
            int newId = serviceDAO.createService(service, uploadPath);
            if (newId != -1) {
                session.setAttribute("success", "Service created successfully!");
            } else {
                session.setAttribute("error", "Failed to create service.");
            }
            
        } catch (SQLException e) {
            System.out.println("Database error creating service: " + e.getMessage());
            session.setAttribute("error", "Database error occurred. Please try again later.");
        } catch (Exception e) {
            System.out.println("Unexpected error creating service: " + e.getMessage());
            session.setAttribute("error", "An unexpected error occurred. Please try again later.");
        }
        
        response.sendRedirect("CA1/adminPage.jsp");
    }
    
    private String getSubmittedFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }
}