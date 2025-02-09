<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="css/registerMember.css">
</head>
<body>
    <div class="auth-container">
        <!-- Left Section -->
        <div class="auth-left">
            <h1>Welcome!</h1>
            <p>Your journey to a cleaner home starts here!</p>
        </div>

        <!-- Right Section -->
        <div class="auth-right">
            <h2>Sign Up</h2>
            <form action="${pageContext.request.contextPath}/RegisterUserServlet" method="post">
                <label for="name">Name</label>
                <input type="text" id="name" name="name" placeholder="Enter your name" required>

                <label for="phone">Phone Number</label>
                <input type="text" id="phone" name="phone" placeholder="+65 12345678"
                    pattern="[\+]65 [0-9]{8}" title="Format: +65 XXXXXXXX" required>

                <label for="email">Email Address</label>
                <input type="email" id="email" name="email" placeholder="Enter your email" required>

                <label for="address">Address</label>
                <textarea id="address" name="address" placeholder="Enter your address" required></textarea>

                <label for="password">Password</label>
                <input type="password" id="password" name="password"
                    pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$"
                    title="Password must be at least 8 characters long and include both letters and numbers"
                    placeholder="Enter a password" required>

                <div class="terms-container">
                    <input type="checkbox" id="terms" name="terms" required>
                    <label for="terms">I agree to the Terms of Service and Privacy Policy. By registering, I consent to receiving cleaning service updates and promotional materials.</label>
                </div>

                <%-- Display error message if available --%>
                <% String error = (String) session.getAttribute("registerError");
                   if (error != null) { %>
                    <p class="error-message"><%= error %></p>
                <% session.removeAttribute("registerError"); } %>

                <input type="submit" value="Register" class="submit-btn">

                <p class="switch-auth">
                    Already have an account? <a href="login.jsp">Login here</a>
                </p>
            </form>
        </div>
    </div>
</body>
</html>