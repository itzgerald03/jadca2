<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.ArrayList, dbaccess.Category" %>
<!DOCTYPE html>
<html>
<head>
    <title>Service Categories</title>
    <link rel="stylesheet" href="css/serviceCategory.css">
</head>
<body>
<%
    // Check if we need to get categories
    if (session.getAttribute("categories") == null) {
        response.sendRedirect(request.getContextPath() + "/CA1/ObtainAllCategoryServlet");
        return;
    }
%>

    <!-- Include Header -->
    <%@ include file="header.jsp" %>

    <section class="categories-section">
        <div class="container">
            <h1>Our Cleaning Services</h1>
            <p>Explore our wide range of cleaning services tailored to meet your needs.</p>
            <div class="categories">
                <%
                Object categoryObj = session.getAttribute("categories");
                List<Category> categories = new ArrayList<>();

                if (categoryObj instanceof List<?>) {
                    List<?> tempList = (List<?>) categoryObj;
                    for (Object obj : tempList) {
                        if (obj instanceof Category) {
                            categories.add((Category) obj);
                        }
                    }
                }

                if (!categories.isEmpty()) {
                    for (Category category : categories) {
                %>
                        <div class="category-card">
                            <h3><%= category.getCategoryName() %></h3>
                            <p>Click to explore more about our <%= category.getCategoryName() %> services.</p>
                            <a href="serviceDetails.jsp?categoryId=<%= category.getId() %>">Learn More</a>
                        </div>
                <%
                    }
                } else {
                %>
                    <div class="error-message">
                        No categories available at the moment.
                        <%= session.getAttribute("error") != null ? session.getAttribute("error") : "" %>
                    </div>
                <%
                }
                %>
            </div>
        </div>
    </section>

    <%@ include file="footer.jsp" %>
</body>
</html>