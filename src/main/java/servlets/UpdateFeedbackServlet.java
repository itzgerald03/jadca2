package servlets;

import dbaccess.Feedback;
import dbaccess.FeedbackDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/UpdateFeedbackServlet")
public class UpdateFeedbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private FeedbackDAO feedbackDAO;

    public void init() {
        feedbackDAO = new FeedbackDAO();
    }

    // Handle GET request to display the update feedback form
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer memberId = (Integer) session.getAttribute("userId");

        if (memberId == null) {
            response.sendRedirect(request.getContextPath() + "/CA1/login.jsp");
            return;
        }

        String feedbackIdParam = request.getParameter("id");

        if (feedbackIdParam == null || feedbackIdParam.isEmpty()) {
            request.setAttribute("errorMessage", "Invalid feedback ID.");
            request.getRequestDispatcher("/CA1/updateFeedback.jsp").forward(request, response);
            return;
        }

        int feedbackId = Integer.parseInt(feedbackIdParam);

        try {
            Feedback feedback = feedbackDAO.getFeedbackById(feedbackId);

            if (feedback == null || feedback.getMemberId() != memberId) {
                request.setAttribute("errorMessage", "Feedback not found or you do not have permission to edit it.");
                request.getRequestDispatcher("/CA1/updateFeedback.jsp").forward(request, response);
            } else {
                request.setAttribute("feedback", feedback);
                request.getRequestDispatcher("/CA1/updateFeedback.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // Handle POST request to update feedback
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer memberId = (Integer) session.getAttribute("userId");

        if (memberId == null) {
            response.sendRedirect(request.getContextPath() + "/CA1/login.jsp");
            return;
        }

        String feedbackIdParam = request.getParameter("id");
        String ratingParam = request.getParameter("rating");
        String comments = request.getParameter("comments");

        if (feedbackIdParam == null || feedbackIdParam.isEmpty() || ratingParam == null || ratingParam.isEmpty()) {
            request.setAttribute("errorMessage", "Invalid input. Please provide valid rating and comments.");
            request.getRequestDispatcher("/CA1/updateFeedback.jsp").forward(request, response);
            return;
        }

        int feedbackId = Integer.parseInt(feedbackIdParam);
        int rating = Integer.parseInt(ratingParam);

        Feedback feedback = new Feedback();
        feedback.setId(feedbackId);
        feedback.setMemberId(memberId);
        feedback.setRating(rating);
        feedback.setComments(comments);

        try {
            feedbackDAO.updateFeedback(feedback);
            response.sendRedirect(request.getContextPath() + "/CA1/display_feedback.jsp");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
