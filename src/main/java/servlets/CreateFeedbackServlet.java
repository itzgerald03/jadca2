package servlets;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dbaccess.Feedback;
import dbaccess.FeedbackDAO;

@WebServlet("/CreateFeedbackServlet")
public class CreateFeedbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private FeedbackDAO feedbackDAO;

    public void init() {
        feedbackDAO = new FeedbackDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer memberId = (Integer) session.getAttribute("userId");

        if (memberId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int bookingId = Integer.parseInt(request.getParameter("booking_id"));
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comments = request.getParameter("comments");

        Feedback feedback = new Feedback();
        feedback.setBookingId(bookingId);
        feedback.setMemberId(memberId);
        feedback.setRating(rating);
        feedback.setComments(comments);

        try {
            feedbackDAO.createFeedback(feedback);
            response.sendRedirect("RetrieveFeedbackServlet");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}