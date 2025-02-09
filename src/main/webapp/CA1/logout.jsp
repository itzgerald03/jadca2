<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Logout</title>
</head>
<body>
<%
    // Invalidate the session
    if (session != null) {
        session.invalidate();
    }

    // Clear cookies
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

    // Redirect to login page
    response.sendRedirect("login.jsp");
%>
</body>
</html>
