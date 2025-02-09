package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dbaccess.ServiceDAO;
import dbaccess.Service;

@WebServlet("/CA1/ObtainServicesByCategoryServlet")
public class ObtainServicesByCategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServiceDAO serviceDAO;

    public void init() throws ServletException {
        serviceDAO = new ServiceDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String categoryIdParam = request.getParameter("categoryId");
        
        try {
            int categoryId = (categoryIdParam != null && !categoryIdParam.isEmpty()) 
                ? Integer.parseInt(categoryIdParam) 
                : 1;
                
            System.out.println("ObtainServicesByCategoryServlet: Fetching services for category ID: " + categoryId);
            
            List<Service> services = serviceDAO.getServicesByCategory(categoryId);
            System.out.println("ObtainServicesByCategoryServlet: Found " + services.size() + " services");
            
            // Store both the services and the current category ID
            session.setAttribute("services", services);
            session.setAttribute("currentCategoryId", categoryIdParam);
            
            response.sendRedirect(request.getContextPath() + "/CA1/serviceDetails.jsp?categoryId=" + categoryId);
            
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            session.setAttribute("error", "Database error occurred: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/CA1/serviceDetails.jsp?categoryId=" + categoryIdParam);
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid category ID");
            response.sendRedirect(request.getContextPath() + "/CA1/serviceDetails.jsp");
        }
    }
}