<%@ page session="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dbaccess.User" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>
     <link rel="stylesheet" href="${pageContext.request.contextPath}/CA1/css/profile.css">
</head>
<body>
<jsp:include page="header.jsp" />

<div class="container">
    <h1>Your Profile</h1>

    <% 
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
        <p class="error"><%= error %></p>
    <% 
        } else {
            User user = (User) request.getAttribute("user");
            if (user != null) { 
    %>
                <div class="profile-details">
                    <p><strong>Name:</strong> <%= user.getName() %></p>
                    <p><strong>Email:</strong> <%= user.getEmail() %></p>
                    <p><strong>Phone:</strong> <%= user.getPhone() %></p>
                    <p><strong>Address:</strong> <%= user.getAddress() %></p>
                    <p><strong>Role:</strong> <%= user.getRole() %></p>
                </div>

                <div class="btn-container">
<a href="${pageContext.request.contextPath}/RetrieveProfileServlet?update=true" class="btn">Update Profile</a>
                </div>
    <% 
            } else { 
    %>
                <p class="error">Unable to load profile information.</p>
    <% 
            }
        } 
    %>
</div>

<jsp:include page="footer.jsp" />
</body>
</html>
