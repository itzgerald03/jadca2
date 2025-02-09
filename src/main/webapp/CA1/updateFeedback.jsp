<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dbaccess.Feedback" %>

<!DOCTYPE html>
<html>
<head>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CA1/css/feedback.css">
    <title>Update Feedback</title>
</head>
<body>
<jsp:include page="header.jsp" />

<div class="container">
    <h1>Update Your Feedback</h1>
    
    <%
        Feedback feedback = (Feedback) request.getAttribute("feedback");
        Integer memberId = (Integer) session.getAttribute("userId");

        if (memberId == null) {
    %>
        <p class="error">You must be logged in to update feedback.</p>
    <%
        } else if (feedback == null) {
    %>
        <p class="error">Feedback not found or you do not have permission to edit it.</p>
    <%
        } else {
    %>
       <form method="post" action="${pageContext.request.contextPath}/UpdateFeedbackServlet">
    <input type="hidden" name="id" value="<%= request.getParameter("id") %>">
    
    <label for="rating">Rating (1-5)</label>
    <input type="number" id="rating" name="rating" value="<%= feedback.getRating() %>" min="1" max="5" required>

    <label for="comments">Comments</label>
    <textarea id="comments" name="comments" rows="4" required><%= feedback.getComments() %></textarea>

    <button type="submit" class="btn">Save Changes</button>
</form>

    <%
        }
    %>
</div>

<jsp:include page="footer.jsp" />
</body>
</html>
