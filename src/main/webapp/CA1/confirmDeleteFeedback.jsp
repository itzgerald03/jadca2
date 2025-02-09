<%@ page import="java.sql.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="css/feedback.css">
    <title>Confirm Delete Feedback</title>
</head>
<body>
    <%@ include file="header.jsp" %>

<div class="container">
    <h1>Confirm Feedback Deletion</h1>
    <%
        Integer feedbackId = Integer.parseInt(request.getParameter("feedback_id"));
        Integer memberId = (Integer) session.getAttribute("userId");

        if (memberId == null) {
            out.println("<p class='error'>You must be logged in to delete feedback.</p>");
        } else {
    %>
        <p>Are you sure you want to delete this feedback? This action cannot be undone.</p>
        <form method="post" action="deleteFeedback.jsp">
            <input type="hidden" name="feedback_id" value="<%= feedbackId %>">
            <button type="submit" class="btn delete-btn">Yes, Delete</button>
        </form>
        <form method="get" action="display_feedback.jsp" style="margin-top: 10px;">
            <button type="submit" class="btn">Cancel</button>
        </form>
    <%
        }
    %>
</div>
    <%@ include file="footer.jsp" %>

</body>
</html>
