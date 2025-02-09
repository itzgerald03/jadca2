<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*,
                 dbaccess.DBConnection,
                 dbaccess.CategoryDAO,
                 dbaccess.Category,
                 dbaccess.ServiceDAO,
                 dbaccess.Service,
                 dbaccess.UserDAO,
                 dbaccess.User,
                 dbaccess.BookingDAO,
                 dbaccess.Booking,
                 dbaccess.BookingDetail,
                 dbaccess.FeedbackDAO,
                 dbaccess.Feedback,
                 java.util.List" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Your Bookings</title>
    <link rel="stylesheet" href="css/trackbooking.css">
</head>
<body>

    <!-- Include Header -->
    <%@ include file="header.jsp" %>

    <div class="container">
        <% 
            if (session.getAttribute("userId") == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            int userID = (int) session.getAttribute("userId");
            BookingDAO bookingDAO = new BookingDAO();
            List<Booking> bookings = bookingDAO.getBookingsByMemberId(userID);
        %>

        <h2>Your Bookings</h2>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Member Name</th>
                        <th>Booking Date</th>
                        <th>Appointment Date</th>
                        <th>Services</th>
                        <th>Total Amount</th>
                        <th>Address</th>
                        <th>Special Requests</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (bookings.isEmpty()) {
                    %>
                        <tr>
                            <td colspan="9" style="text-align:center; font-weight:bold;">No Bookings Found</td>
                        </tr>
                    <%
                        } else {
                            for (Booking booking : bookings) {
                    %>
                        <tr>
                            <td><%= booking.getId() %></td>
                            <td><%= booking.getMemberName() %></td>
                            <td><%= booking.getBookingDate() %></td>
                            <td><%= booking.getAppointmentDate() %></td>
                            <td>
                                <% for (BookingDetail detail : booking.getBookingDetails()) { %>
                                    <%= detail.getServiceName() %> (x<%= detail.getQuantity() %>)<br>
                                <% } %>
                            </td>
                            <td>
                                <% double totalAmount = 0; 
                                   for (BookingDetail detail : booking.getBookingDetails()) {
                                       totalAmount += detail.getTotalPrice();
                                   }
                                %>
                                $<%= String.format("%.2f", totalAmount) %>
                            </td>
                            <td><%= booking.getAddress() != null ? booking.getAddress() : "N/A" %></td>
                            <td><%= booking.getSpecialRequests() != null ? booking.getSpecialRequests() : "N/A" %></td>
                            <td class="status <%= booking.getStatus().toLowerCase() %>"><%= booking.getStatus() %></td>
                        </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>

    <%@ include file="footer.jsp" %>

</body>
</html>
