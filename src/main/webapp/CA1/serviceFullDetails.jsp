<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, dbaccess.Service, dbaccess.ServiceDAO" %>
<!DOCTYPE html>
<html>
<head>
    <title>Service Full Details</title>
    <link rel="stylesheet" href="css/serviceFullDetails.css">
</head>
<body>
    <%@ include file="header.jsp" %>

    <%
        // Get serviceId from URL
        String serviceId = request.getParameter("serviceId");
        Service service = null;
        
        if (serviceId != null && !serviceId.isEmpty()) {
            ServiceDAO serviceDAO = new ServiceDAO();
            try {
                service = serviceDAO.getServiceById(Integer.parseInt(serviceId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (service != null) {
            String imageUrl = request.getContextPath() + "/CA1/" + 
                service.getImageUrl().replace("\\", "/");
    %>
    <section class="service-full-details">
        <div class="container">
            <div class="service-content">
                <div class="service-image-container">
                    <img src="<%= imageUrl %>" alt="<%= service.getServiceName() %>">
                </div>
                <div class="service-info">
                    <h1><%= service.getServiceName() %></h1>
                    <p class="category">Category: <%= service.getCategoryName() %></p>
                    <p class="price">$<%= String.format("%.2f", service.getPrice()) %></p>
                    <div class="description">
                        <h2>Service Description</h2>
                        <p><%= service.getDescription() %></p>
                    </div>
                    
					<div class="action-buttons">
					    <% if (session.getAttribute("userId") != null) { %>
					        <form action="AddToCartServlet" method="POST" class="add-to-cart">
					            <input type="hidden" name="serviceId" value="<%= service.getId() %>">
					            <button type="submit" class="cart-button">Add to Cart</button>
					        </form>
					    <% } else { %>
					        <a href="login.jsp?error=Please log in to book services" 
					           class="login-button">Login to Book</a>
					    <% } %>
					</div>
                </div>
            </div>
        </div>
    </section>
    <% } else { %>
        <div class="error-container">
            <p>Service not found.</p>
        </div>
    <% } %>

    <%@ include file="footer.jsp" %>
</body>
</html>