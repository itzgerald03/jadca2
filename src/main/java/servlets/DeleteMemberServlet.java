package servlets;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dbaccess.UserDAO;

@WebServlet("/DeleteMemberServlet")
public class DeleteMemberServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            if (userDAO.deleteUser(id)) {
                session.setAttribute("success", "Member deleted successfully!");
            } else {
                session.setAttribute("error", "Failed to delete member");
            }
            
        } catch (SQLException e) {
            System.out.println("Database error deleting member: " + e.getMessage());
            session.setAttribute("error", "Database error occurred: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error deleting member: " + e.getMessage());
            session.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
        }
        
        response.sendRedirect("CA1/adminPage.jsp");
    }
}