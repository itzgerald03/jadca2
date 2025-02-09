<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, dbaccess.Feedback, dbaccess.FeedbackDAO, java.util.List" %>

<!DOCTYPE html>
<html>
<head>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CA1/css/feedback.css">
    <title>Client Feedback</title>
</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="container">
        <h1>What Our Clients Say</h1>

        <table>
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Rating</th>
                    <th>Comment</th>
                    <% if (session.getAttribute("userId") != null) { %>
                        <th>Actions</th>
                    <% } %>
                </tr>
            </thead>
            <tbody>
                <%
                    FeedbackDAO feedbackDAO = new FeedbackDAO();
                    List<Feedback> feedbackList = feedbackDAO.getAllFeedback();
                    Integer memberId = (Integer) session.getAttribute("userId");

                    if (feedbackList.isEmpty()) {
                %>
                    <tr>
                        <td colspan="4" style="text-align:center; font-weight:bold;">No feedback available</td>
                    </tr>
                <%
                    } else {
                        for (Feedback feedback : feedbackList) {
                            String truncatedComment = feedback.getComments().length() > 50 ?
                                                      feedback.getComments().substring(0, 50) + "..." :
                                                      feedback.getComments();

                            // Generate star ratings
                            StringBuilder stars = new StringBuilder();
                            for (int i = 1; i <= feedback.getRating(); i++) {
                                stars.append("★");
                            }
                            for (int i = feedback.getRating() + 1; i <= 5; i++) {
                                stars.append("☆");
                            }
                %>
                <tr>
                    <td><%= feedback.getMemberName() %></td>
                    <td><%= stars.toString() %></td>
                    <td><%= truncatedComment %></td>
                    
                    <% if (memberId != null && memberId.equals(feedback.getMemberId())) { %>
                        <td>
                            <!-- Edit Feedback Button -->
                            <form method="get" action="${pageContext.request.contextPath}/UpdateFeedbackServlet" style="display:inline;">
                                <input type="hidden" name="id" value="<%= feedback.getId() %>">
                                <button type="submit" class="btn">Edit</button>
                            </form>

                            <!-- Delete Feedback Button -->
                            <form method="post" action="${pageContext.request.contextPath}/DeleteUserFeedbackServlet" style="display:inline;">
                                <input type="hidden" name="id" value="<%= feedback.getId() %>">
                                <button type="submit" class="btn delete-btn">Delete</button>
                            </form>
                        </td>
                    <% } %>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>

        <div class="btn-container">
            <% if (session.getAttribute("userId") != null) { %>
            <a href="feedback_form.jsp" class="btn">Leave a Review</a>
            <% } else { %>
                <p class="error">You must be logged in to leave a review. <a href="login.jsp">Log in here</a></p>
            <% } %>
        </div>
    </div>

    <%@ include file="footer.jsp" %>
</body>
</html>
