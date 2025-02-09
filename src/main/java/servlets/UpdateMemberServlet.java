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
import dbaccess.User;

@WebServlet("/UpdateMemberServlet")
public class UpdateMemberServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            User user = new User();
            user.setId(id);
            user.setName(request.getParameter("name"));
            user.setEmail(request.getParameter("email"));
            user.setPhone(request.getParameter("phone"));
            user.setAddress(request.getParameter("address"));
            user.setRole(request.getParameter("role"));

            // Validate phone number
            if (!userDAO.isValidPhoneNumber(user.getPhone())) {
                session.setAttribute("error", "Invalid phone number format. Use format: +65 XXXXXXXX");
                response.sendRedirect("CA1/adminPage.jsp");
                return;
            }

            if (userDAO.updateUser(user)) {
                session.setAttribute("success", "Member updated successfully!");
            } else {
                session.setAttribute("error", "Failed to update member");
            }
            
        } catch (SQLException e) {
            System.out.println("Database error updating member: " + e.getMessage());
            session.setAttribute("error", "Database error occurred: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error updating member: " + e.getMessage());
            session.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
        }
        
        response.sendRedirect("CA1/adminPage.jsp");
    }
}