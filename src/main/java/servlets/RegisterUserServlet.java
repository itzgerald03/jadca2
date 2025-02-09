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

@WebServlet("/RegisterUserServlet")
public class RegisterUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Get form data
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            String password = request.getParameter("password");
            String terms = request.getParameter("terms");

            // Validate form data
            if (!validateInputs(name, phone, email, address, password, terms)) {
                session.setAttribute("registerError", "Please fill in all fields correctly.");
                response.sendRedirect("CA1/registerMember.jsp");
                return;
            }

            // Check if user already exists
            if (userDAO.getUserByEmail(email) != null) {
                session.setAttribute("registerError", "Email already registered. Please use a different email.");
                response.sendRedirect("CA1/registerMember.jsp");
                return;
            }

            // Create new user object
            User newUser = new User();
            newUser.setName(name);
            newUser.setPhone(phone);
            newUser.setEmail(email);
            newUser.setAddress(address);
            newUser.setPassword(password);
            newUser.setRole("Customer"); // Default role

            // Save user to database
            int userId = userDAO.createUser(newUser);

            if (userId > 0) {
                // Set session attributes
                session.setAttribute("userId", userId);
                session.setAttribute("userName", name);
                session.setAttribute("userEmail", email);
                session.setAttribute("userRole", "Customer");
                
                // Redirect to success page or home
                response.sendRedirect("CA1/home.jsp");
            } else {
                session.setAttribute("registerError", "Registration failed. Please try again.");
                response.sendRedirect("CA1/registerMember.jsp");
            }

        } catch (SQLException e) {
            System.out.println("Database error during registration: " + e.getMessage());
            session.setAttribute("registerError", "An error occurred during registration. Please try again later.");
            response.sendRedirect("CA1/registerMember.jsp");
        } catch (Exception e) {
            System.out.println("Unexpected error during registration: " + e.getMessage());
            session.setAttribute("registerError", "An unexpected error occurred. Please try again later.");
            response.sendRedirect("CA1/registerMember.jsp");
        }
    }

    private boolean validateInputs(String name, String phone, String email, String address, 
                                 String password, String terms) {
        if (name == null || name.trim().isEmpty() ||
            phone == null || !phone.matches("[\\+]65 [0-9]{8}") ||
            email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$") ||
            address == null || address.trim().isEmpty() ||
            password == null || !password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$") ||
            terms == null) {
            return false;
        }
        return true;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendRedirect("CA1/registerMember.jsp");
    }
}