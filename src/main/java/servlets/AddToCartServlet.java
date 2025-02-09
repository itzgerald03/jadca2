package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dbaccess.Service;
import dbaccess.ServiceDAO;
import dbaccess.CartItem;

@WebServlet("/CA1/AddToCartServlet")
public class AddToCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServiceDAO serviceDAO;

    public void init() throws ServletException {
        serviceDAO = new ServiceDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        HttpSession session = request.getSession();
        
        if (session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp?error=Please log in to add items to cart");
            return;
        }
        
        try {
            int serviceId = Integer.parseInt(request.getParameter("serviceId"));
            Service service = serviceDAO.getServiceById(serviceId);
            
            if (service != null) {
                @SuppressWarnings("unchecked")
                List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
                if (cart == null) {
                    cart = new ArrayList<>();
                    session.setAttribute("cart", cart);
                }
                
                boolean found = false;
                for (CartItem item : cart) {
                    if (item.getServiceId() == serviceId) {
                        item.setQuantity(item.getQuantity() + 1);
                        found = true;
                        break;
                    }
                }
                
                if (!found) {
                    CartItem newItem = new CartItem(
                        service.getId(),
                        service.getServiceName(),
                        service.getDescription(),
                        service.getPrice(),
                        service.getImageUrl()
                    );
                    cart.add(newItem);
                }
                
                session.setAttribute("success", "Service added to cart successfully!");
                response.sendRedirect("cart.jsp");
            } else {
                session.setAttribute("error", "Service not found");
                response.sendRedirect("serviceFullDetails.jsp?serviceId=" + serviceId);
            }
            
        } catch (Exception e) {
            session.setAttribute("error", "Error adding service to cart: " + e.getMessage());
            response.sendRedirect("serviceFullDetails.jsp");
        }
    }
}