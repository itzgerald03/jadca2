<%@ page session="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, dbaccess.BookingDAO, dbaccess.Booking, dbaccess.BookingDetail, java.util.List" %>


<!DOCTYPE html>
<html>
<head>
    <title>Leave Feedback</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CA1/css/feedback_form.css">
    
</head>
<body>
<jsp:include page="header.jsp" />

<div class="container">
    <h1>Leave Feedback</h1>

    <%
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;

        if (userId == null) {
    %>
        <p class="error">You must be logged in to leave feedback. <a href="login.jsp">Log in here</a></p>
    <%
        } else {
            BookingDAO bookingDAO = new BookingDAO();
            List<Booking> completedBookings = bookingDAO.getCompletedBookingsForFeedback(userId);

            if (completedBookings.isEmpty()) {
    %>
                <p>No completed bookings available for feedback.</p>
    <%
            } else {
                for (Booking booking : completedBookings) {
    %>
                <div class="card">
                    <h2>Service: <%= booking.getBookingDetails().get(0).getServiceName() %></h2>
                    <p><strong>Booking Date:</strong> <%= booking.getBookingDate() %></p>
                    <p><strong>Appointment Date:</strong> <%= booking.getAppointmentDate() %></p>
                    <p><strong>Status:</strong> <%= booking.getStatus() %></p>

                    <h3>Booking Details:</h3>
                    <ul>
                        <%
                            for (BookingDetail detail : booking.getBookingDetails()) {
                        %>
                        <li>
                            <strong>Quantity:</strong> <%= detail.getQuantity() %><br>
                            <strong>Total Price:</strong> $<%= String.format("%.2f", detail.getTotalPrice()) %>
                        </li>
                        <%
                            }
                        %>
                    </ul>

        <form action="${pageContext.request.contextPath}/CreateFeedbackServlet" method="post">
                        <input type="hidden" name="booking_id" value="<%= booking.getId() %>">

                        <label for="rating">Rating (1-5)</label>
                        <input type="number" id="rating" name="rating" min="1" max="5" required>

                        <label for="comments">Comments</label>
                        <textarea id="comments" name="comments" rows="4" placeholder="Enter at least 5 characters" required></textarea>

                        <button type="submit">Submit Feedback</button>
                    </form>
                </div>
    <%
                }
            }
        }
    %>
</div>

<jsp:include page="footer.jsp" />
</body>
</html>