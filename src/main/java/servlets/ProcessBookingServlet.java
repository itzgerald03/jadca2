package servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dbaccess.Booking;
import dbaccess.BookingDAO;
import dbaccess.BookingDetail;
import dbaccess.CartItem;
import dbaccess.Payment;
import dbaccess.PaymentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/CA1/ProcessBookingServlet")
public class ProcessBookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookingDAO bookingDAO;
    private PaymentDAO paymentDAO;

    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
        paymentDAO = new PaymentDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Check if user is logged in
            Object userIdObj = session.getAttribute("userId");
            if (userIdObj == null) {
                session.setAttribute("loginError", "Please log in to make a booking");
                response.sendRedirect(request.getContextPath() + "/CA1/login.jsp");
                return;
            }
            
            Integer userId = (Integer) userIdObj;

            // Get form data
            String appointmentDateStr = request.getParameter("appointmentDate");
            String specialRequest = request.getParameter("specialRequest");
            String paymentIntentId = request.getParameter("paymentIntentId");

            if (appointmentDateStr == null || appointmentDateStr.trim().isEmpty()) {
                throw new ServletException("Appointment date is required");
            }

            // Convert appointment date string to Timestamp
            Timestamp appointmentDate = Timestamp.valueOf(appointmentDateStr.replace("T", " ") + ":00");

            // Get cart items from session
            @SuppressWarnings("unchecked")
            List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cart");
            
            if (cartItems == null || cartItems.isEmpty()) {
                throw new ServletException("No items in cart");
            }

            // Calculate total amount
            double totalAmount = cartItems.stream()
                                        .mapToDouble(CartItem::getTotal)
                                        .sum();

            // Create booking
            Booking booking = new Booking();
            booking.setMemberId(userId);
            booking.setBookingDate(Timestamp.valueOf(LocalDateTime.now()));
            booking.setAppointmentDate(appointmentDate);
            booking.setSpecialRequests(specialRequest);
            booking.setStatus("Pending");

            // Create booking details
            List<BookingDetail> bookingDetails = new ArrayList<>();
            for (CartItem item : cartItems) {
                BookingDetail detail = new BookingDetail();
                detail.setServiceId(item.getServiceId());
                detail.setQuantity(item.getQuantity());
                detail.setTotalPrice(item.getTotal());
                bookingDetails.add(detail);
            }
            booking.setBookingDetails(bookingDetails);

            // Save booking
            booking = bookingDAO.createBooking(booking);

            // Create payment record
            Payment payment = new Payment();
            payment.setBookingId(booking.getId());
            payment.setPaymentIntentId(paymentIntentId);
            payment.setAmount(totalAmount);
            payment.setStatus("succeeded");
            paymentDAO.createPayment(payment);

            // Clear the cart
            session.removeAttribute("cart");

            // Store booking ID and success message in session
            session.setAttribute("lastBookingId", booking.getId());
            session.setAttribute("successMessage", "Your booking has been confirmed! Booking Reference: #" + booking.getId());

            // Redirect to confirmation page
            response.sendRedirect(request.getContextPath() + "/CA1/bookingSuccess.jsp");
            
        } catch (Exception e) {
            // Log the error
            getServletContext().log("Error in ProcessBookingServlet: " + e.getMessage(), e);
            
            // Set error message
            session.setAttribute("errorMessage", "Failed to process booking: " + e.getMessage());
            
            // Redirect back to booking page
            response.sendRedirect(request.getContextPath() + "/CA1/serviceBooking.jsp");
        }
    }
}