package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dbaccess.User;
import dbaccess.UserDAO;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/UpdateProfileServlet")
public class UpdateProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        UserDAO userDAO = new UserDAO();
        
        try {
            User user = userDAO.getUserById(userId);
            if (user == null) {
                request.setAttribute("error", "User not found.");
                request.getRequestDispatcher("CA1/profile.jsp").forward(request, response);
                return;
            }

            // Update user object
            user.setName(name);
            user.setEmail(email);
            user.setPhone(phone);
            user.setAddress(address);

            // Perform update
            boolean isUpdated = userDAO.updateUser(user);

            if (isUpdated) {
                session.setAttribute("success", "Profile updated successfully.");
                response.sendRedirect("RetrieveProfileServlet");  // Redirect to reload profile page
            } else {
                request.setAttribute("error", "Failed to update profile. Please try again.");
                request.getRequestDispatcher("CA1/updateProfile.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("CA1/updateProfile.jsp").forward(request, response);
        }
    }
}
