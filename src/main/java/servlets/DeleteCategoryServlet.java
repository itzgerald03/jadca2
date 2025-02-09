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

@WebServlet("/DeleteCategoryServlet")
public class DeleteCategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CategoryDAO categoryDAO;

    public void init() throws ServletException {
        categoryDAO = new CategoryDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String categoryId = request.getParameter("id");
        HttpSession session = request.getSession();
        
        try {
            if (categoryId == null || categoryId.trim().isEmpty()) {
                session.setAttribute("error", "Invalid category ID.");
                response.sendRedirect("CA1/adminPage.jsp");
                return;
            }

            if (categoryDAO.deleteCategory(Integer.parseInt(categoryId))) {
                session.setAttribute("success", "Category deleted successfully!");
            } else {
                session.setAttribute("error", "Failed to delete category.");
            }
            
        } catch (SQLException e) {
            System.out.println("Database error deleting category: " + e.getMessage());
            session.setAttribute("error", "Database error occurred. Please try again later.");
        } catch (Exception e) {
            System.out.println("Unexpected error deleting category: " + e.getMessage());
            session.setAttribute("error", "An unexpected error occurred. Please try again later.");
        }
        
        response.sendRedirect("CA1/adminPage.jsp");
    }
}