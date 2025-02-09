<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="dbaccess.CustomerStats" %>
<!DOCTYPE html>
<html>
<head>
    <title>Top Customers Report</title>
    <link rel="stylesheet" href="css/salesReport.css">
</head>
<body>

    <!-- Include Header -->
    <%@ include file="header.jsp" %>

    <!-- Hero Section -->
    <section class="hero">
        <div class="hero-container">
            <div class="hero-text">
                <h1>Top Customers Report</h1>
                <p>View the top 10 customers by total booking value.</p>
            </div>
        </div>
    </section>

    <!-- Error Message -->
    <% if (request.getAttribute("error") != null) { %>
        <p class="error-message"><%= request.getAttribute("error") %></p>
    <% } %>

    <!-- Top Customers Report Section -->
    <section class="top-customers-report">
        <h2>Top Customers</h2>
        <%
            @SuppressWarnings("unchecked")
            List<CustomerStats> topCustomers = (List<CustomerStats>) request.getAttribute("topCustomers");
            if (topCustomers != null && !topCustomers.isEmpty()) {
        %>
        <table>
            <thead>
                <tr>
                    <th>Customer ID</th>
                    <th>Customer Name</th>
                    <th>Total Bookings</th>
                    <th>Total Booking Value</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (CustomerStats customer : topCustomers) {
                %>
                <tr>
                    <td><%= customer.getCustomerId() %></td>
                    <td><%= customer.getCustomerName() %></td>
                    <td><%= customer.getTotalBookings() %></td>
                    <td>$<%= String.format("%.2f", customer.getTotalBookingValue()) %></td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
        <%
            } else {
        %>
        <p>No top customers data available.</p>
        <%
            }
        %>
    </section>

    <!-- Include Footer -->
    <%@ include file="footer.jsp" %>

</body>
</html>
