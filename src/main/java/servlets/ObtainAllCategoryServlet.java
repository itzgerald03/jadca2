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

@WebServlet("/CA1/ObtainAllCategoryServlet")
public class ObtainAllCategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CategoryDAO categoryDAO;

    public void init() throws ServletException {
        categoryDAO = new CategoryDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        System.out.println("ObtainAllCategoryServlet: Starting category retrieval");

        try {
            List<Category> categories = categoryDAO.getAllCategories();
            System.out.println("Retrieved " + categories.size() + " categories");
            session.setAttribute("categories", categories);
            
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            session.setAttribute("error", "Database error occurred: " + e.getMessage());
        }

        // Redirect back to serviceCategory.jsp in the CA1 folder
        response.sendRedirect(request.getContextPath() + "/CA1/serviceCategory.jsp");
    }
}