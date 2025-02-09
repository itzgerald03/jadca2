package servlets;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dbaccess.Service;
import dbaccess.ServiceDAO;

@WebServlet("/RetrieveServiceServlet")
public class RetrieveServiceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String filter = request.getParameter("filter");
        System.out.println("RetrieveServiceServlet called with filter: " + filter);

        List<Service> services;
        ServiceDAO serviceDAO = new ServiceDAO();
        
        try {
            services = serviceDAO.getAllServices(); // Fetch all services
            
            // Apply sorting filters
            if ("bestRated".equals(filter)) {
                services.sort((s1, s2) -> Double.compare(s2.getAvgRating(), s1.getAvgRating()));
            } else if ("lowestRated".equals(filter)) {
                services.sort((s1, s2) -> Double.compare(s1.getAvgRating(), s2.getAvgRating()));
            } else if ("highDemand".equals(filter)) {
                services.sort((s1, s2) -> Integer.compare(s2.getBookingCount(), s1.getBookingCount()));
            }

            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(generateServiceTable(services));

        } catch (SQLException e) {
            throw new ServletException("Error retrieving services", e);
        }
    }

    // Helper method to generate only the table rows
    private String generateServiceTable(List<Service> services) {
        StringBuilder tableContent = new StringBuilder();
        for (Service service : services) {
            tableContent.append("<tr>")
                        .append("<td>").append(service.getId()).append("</td>")
                        .append("<td>").append(service.getServiceName()).append("</td>")
                        .append("<td>").append(service.getDescription()).append("</td>")
                        .append("<td>$").append(String.format("%.2f", service.getPrice())).append("</td>")
                        .append("<td>").append(service.getCategoryName()).append("</td>")
                        .append("<td><img src='").append(service.getImageUrl()).append("' style='width: 100px; height: auto;'></td>")
                        .append("<td><a href='#' onclick='toggleEditService(").append(service.getId()).append(")'>Edit</a> | ")
                        .append("<a href='DeleteServiceServlet?id=").append(service.getId()).append("' onclick='return confirm(\"Are you sure you want to delete this service?\")'>Delete</a></td>")
                        .append("</tr>");
        }
        return tableContent.toString();
    }
}
