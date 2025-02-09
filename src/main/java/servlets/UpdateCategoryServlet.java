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

@WebServlet("/UpdateCategoryServlet")
public class UpdateCategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CategoryDAO categoryDAO;

    public void init() throws ServletException {
        categoryDAO = new CategoryDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Handle the edit form display
        String categoryId = request.getParameter("id");
        try {
            Category category = categoryDAO.getCategoryById(Integer.parseInt(categoryId));
            request.setAttribute("category", category);
            request.getRequestDispatcher("CA1/editCategory.jsp").forward(request, response);
        } catch (SQLException e) {
            HttpSession session = request.getSession();
            session.setAttribute("error", "Error retrieving category: " + e.getMessage());
            response.sendRedirect("CA1/adminPage.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int categoryId = Integer.parseInt(request.getParameter("id"));
        String categoryName = request.getParameter("categoryName");
        HttpSession session = request.getSession();
        
        try {
            Category category = new Category();
            category.setId(categoryId);
            category.setCategoryName(categoryName);
            
            if (categoryDAO.updateCategory(category)) {
                session.setAttribute("success", "Category updated successfully!");
            } else {
                session.setAttribute("error", "Failed to update category.");
            }
        } catch (SQLException e) {
            session.setAttribute("error", "Database error occurred: " + e.getMessage());
        }
        
        response.sendRedirect("CA1/adminPage.jsp");
    }
}