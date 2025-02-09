<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="css/login.css">
</head>
<body>
    <div class="auth-container">
        <!-- Left Section -->
        <div class="auth-left">
            <h1>Welcome Back!</h1>
            <p>Your spotless journey awaits you.</p>
        </div>

        <!-- Right Section -->
        <div class="auth-right">
            <h2>Log In</h2>
            <form action="${pageContext.request.contextPath}/VerifyUserServlet" method="post">
                <label for="loginid">Email Address</label>
                <input type="email" 
                       id="loginid" 
                       name="loginid" 
                       placeholder="user@example.com" 
                       required>

                <label for="password">Password</label>
                <input type="password" 
                       id="password" 
                       name="password" 
                       placeholder="********" 
                       required>

                <input type="submit" value="Login" class="submit-btn">

                <%-- Display error message if available --%>
                <%
                String loginError = (String) session.getAttribute("loginError");
                if (loginError != null) {
                %>
                    <p class="error-message"><%= loginError %></p>
                <%
                    session.removeAttribute("loginError");
                }
                %>

                <p class="switch-auth">
                    Don't have an account? <a href="registerMember.jsp">Sign up now</a>
                </p>
                <p class="switch-auth">
                    Wish to continue as a guest? <a href="home.jsp">Click here</a>
                </p>
            </form>
        </div>
    </div>
</body>
</html>