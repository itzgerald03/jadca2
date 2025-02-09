<%@ page import="java.util.List, dbaccess.CartItem" %>
<header>
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/CA1/home.jsp" class="logo">Spotless Solutions</a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/CA1/serviceCategory.jsp">Services</a>
            <a href="${pageContext.request.contextPath}/CA1/display_feedback.jsp">Feedback</a>

            <%
            Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
            String userRole = (session != null) ? (String) session.getAttribute("userRole") : null;

            if (userId == null) {
            %>
                <a href="${pageContext.request.contextPath}/CA1/registerMember.jsp">Register</a>
                <a href="${pageContext.request.contextPath}/CA1/login.jsp">Login</a>
            <%
            } else {
            %>
                <a href="${pageContext.request.contextPath}/CA1/trackbooking.jsp">Bookings</a>
                <a href="${pageContext.request.contextPath}/RetrieveProfileServlet">Profile</a>
                <%
                if ("Admin".equals(userRole)) {
                %>
                    <a href="${pageContext.request.contextPath}/CA1/adminPage.jsp">Admin Page</a>
                    <a href="${pageContext.request.contextPath}/CA1/SalesReportServlet">Sales Management</a>
                <%
                }
                %>
                <a href="${pageContext.request.contextPath}/CA1/logout.jsp">Logout</a>
                <a href="${pageContext.request.contextPath}/CA1/cart.jsp" class="cart-icon">
                    <i class="fas fa-shopping-cart"></i>
                    <%
                    @SuppressWarnings("unchecked")
                    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
                    int itemCount = (cart != null) ? cart.size() : 0;
                    if (itemCount > 0) {
                    %>
                        <span class="cart-count"><%= itemCount %></span>
                    <%
                    }
                    %>
                </a>
            <%
            }
            %>
        </div>
    </nav>
</header>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">