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

@WebServlet("/RetrieveProfileServlet")
public class RetrieveProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/CA1/login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        UserDAO userDAO = new UserDAO();

        try {
            User user = userDAO.getUserById(userId);
            if (user != null) {
                request.setAttribute("user", user);  // ✅ Pass user object

                // ✅ Check if updating profile or just viewing
                String page = request.getParameter("update") != null ? "/CA1/updateProfile.jsp" : "/CA1/profile.jsp";
                request.getRequestDispatcher(page).forward(request, response);
            } else {
                request.setAttribute("error", "User not found.");
                request.getRequestDispatcher("/CA1/profile.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Error fetching profile: " + e.getMessage());
            request.getRequestDispatcher("/CA1/profile.jsp").forward(request, response);
        }
    }
}
