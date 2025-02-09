package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dbaccess.BookingDAO;
import dbaccess.Booking;

@WebServlet("/UpdateBookingServlet")
public class UpdateBookingServlet extends HttpServlet {
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
            if (bookingId == null || bookingId.trim().isEmpty()) {
                session.setAttribute("error", "Invalid booking ID.");
                response.sendRedirect("CA1/adminPage.jsp");
                return;
            }
            
            Booking booking = bookingDAO.getBookingById(Integer.parseInt(bookingId));
            if (booking != null) {
                request.setAttribute("booking", booking);
                request.getRequestDispatcher("CA1/editBooking.jsp").forward(request, response);
            } else {
                session.setAttribute("error", "Booking not found.");
                response.sendRedirect("CA1/adminPage.jsp");
            }
        } catch (SQLException e) {
            System.out.println("Database error retrieving booking: " + e.getMessage());
            session.setAttribute("error", "Database error occurred. Please try again later.");
            response.sendRedirect("CA1/adminPage.jsp");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        try {
            // Get and validate parameters
            String bookingId = request.getParameter("id");
            String appointmentDateStr = request.getParameter("appointmentDate");
            String specialRequests = request.getParameter("specialRequests");
            String status = request.getParameter("status");
            
            if (bookingId == null || appointmentDateStr == null || status == null) {
                session.setAttribute("error", "Missing required fields.");
                response.sendRedirect("CA1/adminPage.jsp");
                return;
            }
            
            // Convert appointment date string to Timestamp
            appointmentDateStr = appointmentDateStr.replace("T", " ") + ":00";
            Timestamp appointmentDate = Timestamp.valueOf(appointmentDateStr);
            
            // Create and populate Booking object
            Booking booking = new Booking();
            booking.setId(Integer.parseInt(bookingId));
            booking.setAppointmentDate(appointmentDate);
            booking.setSpecialRequests(specialRequests);
            booking.setStatus(status);
            
            // Update booking
            if (bookingDAO.updateBooking(booking)) {
                session.setAttribute("success", "Booking updated successfully!");
            } else {
                session.setAttribute("error", "Failed to update booking.");
            }
            
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", "Invalid input format: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database error updating booking: " + e.getMessage());
            session.setAttribute("error", "Database error occurred. Please try again later.");
        } catch (Exception e) {
            System.out.println("Unexpected error updating booking: " + e.getMessage());
            session.setAttribute("error", "An unexpected error occurred. Please try again later.");
        }
        
        response.sendRedirect("CA1/adminPage.jsp");
    }
}