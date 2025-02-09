<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.ArrayList, dbaccess.Service" %>
<!DOCTYPE html>
<html>
<head>
    <title>Service Details</title>
    <link rel="stylesheet" href="css/serviceDetails.css">
</head>
<body>
    <%
    // Get categoryId from URL
    String categoryId = request.getParameter("categoryId");
    // Clear previous services from session when category changes
    String sessionCategoryId = (String) session.getAttribute("currentCategoryId");
    if (sessionCategoryId == null || !sessionCategoryId.equals(categoryId)) {
        session.removeAttribute("services");
    }
    
    // Check if we need to get services
    if (session.getAttribute("services") == null) {
        session.setAttribute("currentCategoryId", categoryId);
        response.sendRedirect(request.getContextPath() + 
            "/CA1/ObtainServicesByCategoryServlet?categoryId=" + categoryId);
        return;
    }
    %>

    <!-- Include Header -->
    <%@ include file="header.jsp" %>

    <!-- Main Content -->
    <section class="service-details-section">
        <div class="container">
            <h1>Available Services</h1>
            <p>Browse through the services we offer under this category and choose the one that suits your needs.</p>
            
            <div class="service-cards">
                <%
                // Safe type checking for services
                List<Service> services = new ArrayList<>();
                Object servicesObj = session.getAttribute("services");
                
                if (servicesObj instanceof List<?>) {
                    List<?> tempList = (List<?>) servicesObj;
                    // Verify each element is a Service
                    for (Object obj : tempList) {
                        if (obj instanceof Service) {
                            services.add((Service) obj);
                        }
                    }
                }
                
                if (services.isEmpty()) {
                %>
                    <div class="service-details-section">
                        <p class="no-services-message">No services are currently available for this category.</p>
                    </div>
                <%
                } else {
                    for (Service service : services) {
                        String imageUrl = request.getContextPath() + "/CA1/" + 
                            service.getImageUrl().replace("\\", "/");
                %>
					<div class="service-card">
					    <img src="<%= imageUrl %>" alt="<%= service.getServiceName() %>" class="service-image">
					    <h2 class="service-title"><%= service.getServiceName() %></h2>
					    <a href="serviceFullDetails.jsp?serviceId=<%= service.getId() %>" class="service-button">
					        View Details
					    </a>
					</div>
                <%
                    }
                }
                
                // Display error message if exists
                String error = (String) session.getAttribute("error");
                if (error != null) {
                %>
                    <div class="error-message"><%= error %></div>
                <%
                    session.removeAttribute("error"); // Clear the error after displaying
                }
                %>
            </div>
        </div>
    </section>

    <!-- Include Footer -->
    <%@ include file="footer.jsp" %>

</body>
</html>