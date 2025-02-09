package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dbaccess.User;
import dbaccess.UserDAO;

@WebServlet("/FilterUsersByAddressServlet")
public class FilterUsersByAddressServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String searchQuery = request.getParameter("searchQuery");
        String sortBy = request.getParameter("sort");

        try {
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.getAllUsers();

            if (sortBy != null && sortBy.equals("address")) {
                // Sort members alphabetically by address (A-Z)
                users = users.stream()
                    .sorted((u1, u2) -> u1.getAddress().compareToIgnoreCase(u2.getAddress()))
                    .collect(Collectors.toList());
            } else if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                // Filter by name search
                users = users.stream()
                    .filter(user -> user.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                    .sorted((u1, u2) -> getRelevanceScore(u2.getName(), searchQuery) - getRelevanceScore(u1.getName(), searchQuery))
                    .collect(Collectors.toList());
            }

            // Return the generated table rows as HTML
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(generateUserTable(users, request));

        } catch (SQLException e) {
            throw new ServletException("Error retrieving users", e);
        }
    }

    // Relevance score for name search
    private int getRelevanceScore(String name, String searchQuery) {
        if (name.equalsIgnoreCase(searchQuery)) {
            return 2;
        } else if (name.toLowerCase().startsWith(searchQuery.toLowerCase())) {
            return 1;
        }
        return 0;
    }

    // Generates table rows for users without an Edit button (only Delete)
    private String generateUserTable(List<User> users, HttpServletRequest request) {
        StringBuilder tableContent = new StringBuilder();
        if (users.isEmpty()) {
            tableContent.append("<tr><td colspan='7' style='color: red;'>No users found.</td></tr>");
        } else {
            // Use the context path dynamically
            String contextPath = request.getContextPath();
            for (User user : users) {
                tableContent.append("<tr>")
                            .append("<td>").append(user.getId()).append("</td>")
                            .append("<td>").append(user.getName()).append("</td>")
                            .append("<td>").append(user.getEmail()).append("</td>")
                            .append("<td>").append(user.getPhone()).append("</td>")
                            .append("<td>").append(user.getAddress()).append("</td>")
                            .append("<td>").append(user.getRole()).append("</td>")
                            // Only include the Delete link here
                            .append("<td><a href='").append(contextPath)
                            .append("/DeleteMemberServlet?id=").append(user.getId())
                            .append("' onclick='return confirm(\"Are you sure you want to delete this member?\")'>Delete</a></td>")
                            .append("</tr>");
            }
        }
        return tableContent.toString();
    }
}
