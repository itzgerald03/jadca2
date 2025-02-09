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

@WebServlet("/CA1/RemoveFromCartServlet")
public class RemoveFromCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        HttpSession session = request.getSession();
        
        try {
            int serviceId = Integer.parseInt(request.getParameter("serviceId"));
            
            @SuppressWarnings("unchecked")
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            
            if (cart != null) {
                cart.removeIf(item -> item.getServiceId() == serviceId);
            }
            
            session.setAttribute("success", "Item removed from cart");
            
        } catch (Exception e) {
            session.setAttribute("error", "Error removing item from cart: " + e.getMessage());
        }
        
        response.sendRedirect("cart.jsp");
    }
}