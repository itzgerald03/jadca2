package servlets;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dbaccess.FeedbackDAO;

@WebServlet("/DeleteFeedbackServlet")
public class DeleteFeedbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private FeedbackDAO feedbackDAO;
    
    public void init() throws ServletException {
        feedbackDAO = new FeedbackDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String feedbackId = request.getParameter("id");
        HttpSession session = request.getSession();
        
        try {
            if (feedbackId == null || feedbackId.trim().isEmpty()) {
                session.setAttribute("error", "Invalid feedback ID.");
                response.sendRedirect("CA1/adminPage.jsp");
                return;
            }
            
            if (feedbackDAO.deleteFeedback(Integer.parseInt(feedbackId))) {
                session.setAttribute("success", "Feedback deleted successfully!");
            } else {
                session.setAttribute("error", "Failed to delete feedback.");
            }
            
        } catch (SQLException e) {
            System.out.println("Database error deleting feedback: " + e.getMessage());
            session.setAttribute("error", "Database error occurred. Please try again later.");
        } catch (Exception e) {
            System.out.println("Unexpected error deleting feedback: " + e.getMessage());
            session.setAttribute("error", "An unexpected error occurred. Please try again later.");
        }
        
        response.sendRedirect("CA1/adminPage.jsp");
    }
}