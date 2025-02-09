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

@WebServlet("/FilterUsersByPostalServlet")
public class FilterUsersByPostalServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String searchQuery = request.getParameter("searchQuery");
        String sortBy = request.getParameter("sort");

        try {
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.getAllUsers();

            if (sortBy != null && sortBy.equals("postal")) {
                // Sort members by extracted postal code, handling missing values properly
                users = users.stream()
                    .sorted((u1, u2) -> {
                        int postal1 = extractPostalCodeAsInt(u1.getAddress());
                        int postal2 = extractPostalCodeAsInt(u2.getAddress());

                        return Integer.compare(postal1, postal2);
                    })
                    .collect(Collectors.toList());

            } else if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                // Filter by name search
                users = users.stream()
                    .filter(user -> user.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                    .sorted((u1, u2) -> getRelevanceScore(u2.getName(), searchQuery) - getRelevanceScore(u1.getName(), searchQuery))
                    .collect(Collectors.toList());
            }

            // If no search or filter is applied, return the full user list
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(generateUserTable(users));

        } catch (SQLException e) {
            throw new ServletException("Error retrieving users", e);
        }
    }

    /**
     * Extracts postal code from the address and converts it to an integer.
     * If no valid postal code is found, return a high number (e.g., 999999) to push it to the bottom.
     */
    private int extractPostalCodeAsInt(String address) {
        if (address == null || address.isEmpty()) {
            return 999999; // Push entries with no postal code to the bottom
        }

        String[] parts = address.split(" ");
        for (int i = parts.length - 1; i >= 0; i--) {
            if (parts[i].matches("\\d{6}")) { // Match 6-digit Singapore postal codes
                return Integer.parseInt(parts[i]); // Convert to integer for proper sorting
            }
        }
        return 999999; // Default for missing postal code
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

    // Generates table rows for users
    private String generateUserTable(List<User> users) {
        StringBuilder tableContent = new StringBuilder();
        if (users.isEmpty()) {
            tableContent.append("<tr><td colspan='7' style='color: red;'>No users found.</td></tr>");
        } else {
            for (User user : users) {
                tableContent.append("<tr>")
                            .append("<td>").append(user.getId()).append("</td>")
                            .append("<td>").append(user.getName()).append("</td>")
                            .append("<td>").append(user.getEmail()).append("</td>")
                            .append("<td>").append(user.getPhone()).append("</td>")
                            .append("<td>").append(user.getAddress()).append("</td>")
                            .append("<td>").append(user.getRole()).append("</td>")
                            .append("<td><a href='#' onclick='toggleEditMember(").append(user.getId()).append(")'>Edit</a> | ")
                            .append("<a href='DeleteMemberServlet?id=").append(user.getId()).append("' onclick='return confirm(\"Are you sure?\")'>Delete</a></td>")
                            .append("</tr>");
            }
        }
        return tableContent.toString();
    }
}
