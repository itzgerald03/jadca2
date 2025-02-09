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

@WebServlet("/VerifyUserServlet")
public class VerifyUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("loginid");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();

        try {
            // Basic validation
            if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
                session.setAttribute("loginError", "Please provide both email and password.");
                response.sendRedirect("CA1/login.jsp");
                return;
            }

            // Verify user credentials using UserDAO
            User user = userDAO.verifyUser(email, password);

            if (user != null) {
                // Set session attributes
                session.setAttribute("userId", user.getId());
                session.setAttribute("userName", user.getName());
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("userRole", user.getRole());

                // Set session timeout (30 minutes)
                session.setMaxInactiveInterval(30 * 60);

                // Redirect based on role with correct path
                if ("Admin".equalsIgnoreCase(user.getRole())) {
                    response.sendRedirect("CA1/adminPage.jsp");
                } else {
                    response.sendRedirect("CA1/home.jsp");
                }
            } else {
                session.setAttribute("loginError", "Invalid email or password.");
                response.sendRedirect("CA1/login.jsp");
            }

        } catch (SQLException e) {
            System.out.println("Database error during login: " + e.getMessage());
            session.setAttribute("loginError", "An error occurred during login. Please try again later.");
            response.sendRedirect("CA1/login.jsp");
        } catch (Exception e) {
            System.out.println("Unexpected error during login: " + e.getMessage());
            session.setAttribute("loginError", "An unexpected error occurred. Please try again later.");
            response.sendRedirect("CA1/login.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirect GET requests to login page
        response.sendRedirect("CA1/login.jsp");
    }
}