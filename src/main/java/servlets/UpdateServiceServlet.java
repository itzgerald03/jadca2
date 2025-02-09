package servlets;

import java.io.*;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;
import dbaccess.ServiceDAO;
import dbaccess.Service;

@WebServlet("/UpdateServiceServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,    // 1 MB
    maxFileSize = 1024 * 1024 * 10,     // 10 MB
    maxRequestSize = 1024 * 1024 * 15    // 15 MB
)
public class UpdateServiceServlet extends HttpServlet {
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
            int serviceId = Integer.parseInt(request.getParameter("id"));
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
            
            // Parse numeric values
            double price = Double.parseDouble(priceStr);
            int categoryId = Integer.parseInt(categoryIdStr);
            
            // Get existing service
            Service service = serviceDAO.getServiceById(serviceId);
            String imageUrl = service.getImageUrl(); // Keep existing image by default
            
            // Handle new image if uploaded
            Part filePart = request.getPart("serviceImage");
            if (filePart != null && filePart.getSize() > 0) {
                // Delete old image
                if (service.getImageUrl() != null) {
                    String oldImagePath = getServletContext().getRealPath("/") + "CA1" + service.getImageUrl();
                    File oldImage = new File(oldImagePath);
                    if (oldImage.exists()) {
                        oldImage.delete();
                    }
                }
                
                // Save new image
                String fileName = getSubmittedFileName(filePart);
                String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
                String uploadPath = getServletContext().getRealPath("/") + "CA1/images/";
                imageUrl = "/images/" + uniqueFileName;
                
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                
                filePart.write(uploadPath + uniqueFileName);
            }
            
            // Update service object
            service.setServiceName(serviceName);
            service.setDescription(description);
            service.setPrice(price);
            service.setCategoryId(categoryId);
            service.setImageUrl(imageUrl);
            
            // Update in database
            if (serviceDAO.updateService(service)) {
                session.setAttribute("success", "Service updated successfully!");
            } else {
                session.setAttribute("error", "Failed to update service.");
            }
            
        } catch (SQLException e) {
            System.out.println("Database error updating service: " + e.getMessage());
            session.setAttribute("error", "Database error occurred. Please try again later.");
        } catch (Exception e) {
            System.out.println("Unexpected error updating service: " + e.getMessage());
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