package servlets;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dbaccess.BookingDAO;
import dbaccess.CustomerStats;

@WebServlet("/CA1/SalesReportServlet")
public class SalesReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("userRole");

        // Ensure the user is an admin
        if (userRole == null || !"Admin".equalsIgnoreCase(userRole)) {
            request.setAttribute("error", "Unauthorized access. Admins only.");
            request.getRequestDispatcher("/CA1/salesReport.jsp").forward(request, response);
            return;
        }

        BookingDAO bookingDAO = new BookingDAO();
        try {
            // Fetch top customers data
            List<CustomerStats> topCustomers = bookingDAO.getTopCustomersByValue(10);

            if (topCustomers.isEmpty()) {
                request.setAttribute("error", "No top customers data available.");
            } else {
                request.setAttribute("topCustomers", topCustomers);
            }

            // Forward to JSP
            request.getRequestDispatcher("/CA1/salesReport.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error generating report: " + e.getMessage());
            request.getRequestDispatcher("/CA1/salesReport.jsp").forward(request, response);
        }
    }
}

