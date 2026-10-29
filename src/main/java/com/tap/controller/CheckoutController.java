
package com.tap.controller;

import java.io.IOException;
//import java.security.Timestamp;
import java.sql.Timestamp;

import com.tap.dao.OrderDAO;
import com.tap.dao.OrderItemDAO;
import com.tap.daoImpl.OrderDAOImpl;
import com.tap.daoImpl.OrderItemDAOImpl;
import com.tap.model.Cart;
import com.tap.model.CartItem;
import com.tap.model.Order;
import com.tap.model.OrderItem;
import com.tap.model.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // Check whether the user is logged in
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Get logged-in user
        User user = (User) session.getAttribute("user");

        // Get cart from session
        Cart cart = (Cart) session.getAttribute("cart");

        // Check whether cart is empty
        if (cart == null
                || cart.getItems() == null
                || cart.getItems().isEmpty()) {

            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        // Provide cart and user details to checkout.jsp
        req.setAttribute("cart", cart);
        req.setAttribute("checkoutUser", user);
        req.setAttribute("subtotal", cart.getTotal());
        req.setAttribute("discount", cart.getDiscountAmount());
        req.setAttribute("finalTotal", cart.getFinalTotal());

        // Forward to checkout page
        RequestDispatcher dispatcher =
                req.getRequestDispatcher("/checkout.jsp");

        dispatcher.forward(req, resp);
    }


@Override
protected void doPost(HttpServletRequest req,
                      HttpServletResponse resp)
        throws ServletException, IOException {

    // Get logged-in user
    HttpSession session = req.getSession(false);

    if (session == null || session.getAttribute("user") == null) {
        resp.sendRedirect(req.getContextPath() + "/login");
        return;
    }

    User loggedInUser = (User) session.getAttribute("user");

    // Get cart
    Cart cart = (Cart) session.getAttribute("cart");

    if (cart == null || cart.getItems() == null
            || cart.getItems().isEmpty()) {
        resp.sendRedirect(req.getContextPath() + "/cart");
        return;
    }

    // Get checkout details
    String deliveryAddress = req.getParameter("deliveryAddress");
    String phoneNumber = req.getParameter("phoneNumber");
    String paymentMode = req.getParameter("paymentMode");

    // Validate details
    if (deliveryAddress == null
            || deliveryAddress.trim().isEmpty()
            || deliveryAddress.trim().length() > 500
            || phoneNumber == null
            || !phoneNumber.matches("[0-9]{10}")
            || (!"COD".equals(paymentMode)
                && !"ONLINE".equals(paymentMode))) {

        req.setAttribute("error",
                "Please enter valid delivery details and payment method.");
        req.setAttribute("cart", cart);
        req.setAttribute("checkoutUser", loggedInUser);

        req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
        return;
    }
    
 // Calculate checkout amounts after validating details
    double subtotal = cart.getTotal();
    double discount = cart.getDiscountAmount();
    double finalTotal = cart.getFinalTotal();

    // Create Order DAO
    OrderDAO orderDao = new OrderDAOImpl();

    // Create Order object
    Order order = new Order();
    order.setRestaurantId(cart.getRestaurantId());
    order.setUserId(loggedInUser.getUserID());
    order.setTotalAmount(cart.getFinalTotal());
    order.setStatus("PLACED");
    order.setPaymentMode(paymentMode);
    order.setOrderDate(new Timestamp(System.currentTimeMillis()));

    // Insert order and get generated order ID
    int orderId = orderDao.addOrder(order);

    if (orderId <= 0) {
        req.setAttribute("error",
                "Unable to place your order. Please try again.");
        req.setAttribute("cart", cart);
        req.setAttribute("checkoutUser", loggedInUser);

        req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
        return;
    }

    // Create OrderItem DAO
    OrderItemDAO orderItemDao = new OrderItemDAOImpl();

    // Insert each cart item into the OrderItem table
    for (CartItem cartItem : cart.getItems().values()) {

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderId);
        orderItem.setMenuId(cartItem.getMenu().getMenuId());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setTotalAmount(cartItem.getSubtotal());

        orderItemDao.addOrderItem(orderItem);
    }

    // Clear cart after placing the order
    cart.clearCart();
    session.setAttribute("cart", cart);

    // Store order ID for the success page
    session.setAttribute("lastOrderId", orderId);

    // Redirect to success page
    resp.sendRedirect(req.getContextPath() + "/order-success");
}

}
