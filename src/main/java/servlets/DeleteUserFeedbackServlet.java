package servlets;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dbaccess.FeedbackDAO;

@WebServlet("/DeleteUserFeedbackServlet")
public class DeleteUserFeedbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private FeedbackDAO feedbackDAO;

    public void init() {
        feedbackDAO = new FeedbackDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int feedbackId = Integer.parseInt(request.getParameter("id"));

        try {
            boolean deleted = feedbackDAO.deleteFeedback(feedbackId);
            if (deleted) {
                request.getSession().setAttribute("successMessage", "Feedback deleted successfully.");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to delete feedback.");
            }
        } catch (SQLException e) {
            throw new ServletException("Database error when deleting feedback", e);
        }

        // Redirect back to feedback display page
        response.sendRedirect(request.getContextPath() + "/RetrieveFeedbackServlet");
    }
}
