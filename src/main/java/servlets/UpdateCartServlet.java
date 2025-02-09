package servlets;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dbaccess.CartItem;

@WebServlet("/CA1/UpdateCartServlet")
public class UpdateCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        HttpSession session = request.getSession();
        
        try {
            int serviceId = Integer.parseInt(request.getParameter("serviceId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }
            
            @SuppressWarnings("unchecked")
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            
            if (cart != null) {
                for (CartItem item : cart) {
                    if (item.getServiceId() == serviceId) {
                        item.setQuantity(quantity);
                        break;
                    }
                }
            }
            
            session.setAttribute("success", "Cart updated successfully!");
            
        } catch (Exception e) {
            session.setAttribute("error", "Error updating cart: " + e.getMessage());
        }
        
        response.sendRedirect("cart.jsp");
    }
}