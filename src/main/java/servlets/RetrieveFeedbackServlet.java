package servlets;

import dbaccess.Feedback;
import dbaccess.FeedbackDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/RetrieveFeedbackServlet")
public class RetrieveFeedbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private FeedbackDAO feedbackDAO;

    public void init() {
        feedbackDAO = new FeedbackDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Feedback> feedbackList = feedbackDAO.getAllFeedback();
            request.setAttribute("feedbackList", feedbackList);
            response.sendRedirect(request.getContextPath() + "/CA1/display_feedback.jsp");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
