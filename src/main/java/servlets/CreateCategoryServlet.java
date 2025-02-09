package servlets;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dbaccess.CategoryDAO;
import dbaccess.Category;

@WebServlet("/CreateCategoryServlet")
public class CreateCategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CategoryDAO categoryDAO;

    public void init() throws ServletException {
        categoryDAO = new CategoryDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String categoryName = request.getParameter("categoryName");
        HttpSession session = request.getSession();
        
        try {
            // Basic validation
            if (categoryName == null || categoryName.trim().isEmpty()) {
                session.setAttribute("error", "Category name cannot be empty.");
                response.sendRedirect("CA1/adminPage.jsp");
                return;
            }

            Category category = new Category();
            category.setCategoryName(categoryName);
            
            int newId = categoryDAO.createCategory(category);
            if (newId != -1) {
                session.setAttribute("success", "Category created successfully!");
            } else {
                session.setAttribute("error", "Failed to create category.");
            }
            
        } catch (SQLException e) {
            System.out.println("Database error creating category: " + e.getMessage());
            session.setAttribute("error", "Database error occurred. Please try again later.");
        } catch (Exception e) {
            System.out.println("Unexpected error creating category: " + e.getMessage());
            session.setAttribute("error", "An unexpected error occurred. Please try again later.");
        }
        
        response.sendRedirect("CA1/adminPage.jsp");
    }
}