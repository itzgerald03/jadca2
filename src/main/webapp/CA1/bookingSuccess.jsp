<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Booking Successful</title>
    <link rel="stylesheet" href="css/bookingSuccess.css">
    
</head>

	
<body>
    <h1>Thank You!</h1>
    <p>Your booking has been successfully placed. You will receive a confirmation email shortly.</p>
    <p>You will be redirected back to the home page in 3 seconds...</p>
    <% response.setHeader("Refresh", "3; URL=home.jsp"); %>
    <a href="home.jsp">Return to Home</a>
    

</body>
</html>
