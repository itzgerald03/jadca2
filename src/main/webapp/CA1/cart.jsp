<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, dbaccess.CartItem" %>
<!DOCTYPE html>
<html>
<head>
    <title>Shopping Cart</title>
    <link rel="stylesheet" href="css/cart.css">
</head>
<body>
    <%@ include file="header.jsp" %>

    <section class="cart-section">
        <div class="container">
            <h1>Shopping Cart</h1>

            <%
            @SuppressWarnings("unchecked")
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            if (cart == null || cart.isEmpty()) {
            %>
            <div class="empty-cart">
                <p>Your cart is empty</p>
                <a href="serviceCategory.jsp" class="continue-shopping">Continue Shopping</a>
            </div>
            <% } else { %>
            <div class="cart-items">
                <%
                double total = 0;
                for (CartItem item : cart) {
                    total += item.getTotal();
                %>
                <div class="cart-item">
                    <img src="<%= request.getContextPath() + "/CA1/" + item.getImageUrl().replace("\\", "/") %>"
                         alt="<%= item.getServiceName() %>">
                    <div class="item-details">
                        <h3><%= item.getServiceName() %></h3>
                        <p><%= item.getDescription() %></p>
                        <p>Price: $<%= String.format("%.2f", item.getPrice()) %></p>
                        <form action="UpdateCartServlet" method="POST" class="quantity-form">
                            <input type="hidden" name="serviceId" value="<%= item.getServiceId() %>">
                            <input type="number" name="quantity" value="<%= item.getQuantity() %>" min="1" max="10">
                            <button type="submit">Update</button>
                        </form>
                        <form action="RemoveFromCartServlet" method="POST" class="remove-form">
                            <input type="hidden" name="serviceId" value="<%= item.getServiceId() %>">
                            <button type="submit">Remove</button>
                        </form>
                    </div>
                    <div class="item-total">
                        $<%= String.format("%.2f", item.getTotal()) %>
                    </div>
                </div>
                <% } %>

                <div class="cart-summary">
                    <h3>Total: $<%= String.format("%.2f", total) %></h3>
                    <!-- Modified to link directly to serviceBooking.jsp with the service details -->
                    <% 
                    // Assuming we're booking the first item in cart
                    if (!cart.isEmpty()) {
                        CartItem firstItem = cart.get(0);
                    %>
                    <a href="serviceBooking.jsp?serviceName=<%= firstItem.getServiceName() %>" 
                       class="checkout-button">Proceed to Book Service</a>
                    <% } %>
                    <a href="serviceCategory.jsp" class="continue-shopping">Continue Shopping</a>
                </div>
            </div>
            <% } %>
        </div>
    </section>

    <%@ include file="footer.jsp" %>
</body>
</html>