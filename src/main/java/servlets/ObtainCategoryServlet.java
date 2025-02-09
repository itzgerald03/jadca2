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
import dbaccess.CategoryDAO;
import dbaccess.Category;

@WebServlet("/ObtainCategoryServlet")
public class ObtainCategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CategoryDAO categoryDAO;

    public void init() throws ServletException {
        categoryDAO = new CategoryDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            List<Category> categories = categoryDAO.getAllCategories();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("CA1/adminPage.jsp").forward(request, response);
            
        } catch (SQLException e) {
            System.out.println("Database error fetching categories: " + e.getMessage());
            session.setAttribute("error", "Database error occurred. Please try again later.");
            response.sendRedirect("CA1/adminPage.jsp");
        } catch (Exception e) {
            System.out.println("Unexpected error fetching categories: " + e.getMessage());
            session.setAttribute("error", "An unexpected error occurred. Please try again later.");
            response.sendRedirect("CA1/adminPage.jsp");
        }
    }
}