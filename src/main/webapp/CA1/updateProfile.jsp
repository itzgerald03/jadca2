<%@ page session="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dbaccess.User" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CA1/css/profile.css">
</head>
<body>
<jsp:include page="header.jsp" />

<div class="container">
    <h1>Update Profile</h1>

    <% 
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
        <p class="error"><%= error %></p>
    <% 
        }
        User user = (User) request.getAttribute("user");
        if (user != null) { 
    %>
        <form action="${pageContext.request.contextPath}/UpdateProfileServlet" method="post">
            <label>Name:</label>
            <input type="text" name="name" value="<%= user.getName() %>" required>

            <label>Email:</label>
            <input type="email" name="email" value="<%= user.getEmail() %>" required>

            <label>Phone:</label>
            <input type="text" name="phone" value="<%= user.getPhone() %>" required>

            <label>Address:</label>
            <input type="text" name="address" value="<%= user.getAddress() %>" required>

            <button type="submit">Save Changes</button>
            <a href="profile.jsp" class="btn">Cancel</a>
        </form>
    <% 
        } else { 
    %>
        <p class="error">Unable to load profile information.</p>
    <% 
        }
    %>
</div>

<jsp:include page="footer.jsp" />
</body>
</html>
