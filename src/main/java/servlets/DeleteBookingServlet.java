package servlets;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dbaccess.BookingDAO;

@WebServlet("/DeleteBookingServlet")
public class DeleteBookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookingDAO bookingDAO;
    
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String bookingId = request.getParameter("id");
        HttpSession session = request.getSession();
        
        try {
            // Validate booking ID
            if (bookingId == null || bookingId.trim().isEmpty()) {
                session.setAttribute("error", "Invalid booking ID.");
                response.sendRedirect("CA1/adminPage.jsp");
                return;
            }
            
            // Check if the booking has associated feedback
            // You might want to handle this differently based on your requirements
            
            // Delete the booking
            if (bookingDAO.deleteBooking(Integer.parseInt(bookingId))) {
                session.setAttribute("success", "Booking deleted successfully!");
            } else {
                session.setAttribute("error", "Failed to delete booking.");
            }
            
        } catch (SQLException e) {
            System.out.println("Database error deleting booking: " + e.getMessage());
            session.setAttribute("error", "Database error occurred. Please try again later.");
        } catch (Exception e) {
            System.out.println("Unexpected error deleting booking: " + e.getMessage());
            session.setAttribute("error", "An unexpected error occurred. Please try again later.");
        }
        
        response.sendRedirect("CA1/adminPage.jsp");
    }
}