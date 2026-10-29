
package com.tap.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tap.daoImpl.OrderDAOImpl;
import com.tap.model.Cart;
import com.tap.model.CartItem;
import com.tap.model.Order;
import com.tap.model.OrderItem;
import com.tap.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/order")
public class OrderController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // Check whether the user is logged in
        if (session == null
                || session.getAttribute("user") == null) {

            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        Cart cart = (Cart) session.getAttribute("cart");

        // Check whether the cart contains items
        if (cart == null
                || cart.getItems() == null
                || cart.getItems().isEmpty()) {

            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        // Get the restaurant associated with the cart
        int restaurantId = cart.getRestaurantId();

        if (restaurantId <= 0) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        // Get checkout details submitted by confirmorder.jsp
        String address = req.getParameter("deliveryAddress");
        String phone = req.getParameter("phoneNumber");
        String paymentMode = req.getParameter("paymentMode");

        // Validate submitted details
        if (address == null
                || address.trim().isEmpty()
                || address.trim().length() > 500
                || phone == null
                || !phone.matches("[0-9]{10}")
                || (!"COD".equals(paymentMode)
                    && !"ONLINE".equals(paymentMode))) {

            resp.sendRedirect(req.getContextPath() + "/checkout");
            return;
        }

        // Calculate amounts from the server-side cart
        double subtotal = cart.getTotal();
        double discount = cart.getDiscountAmount();
        double finalTotal = cart.getFinalTotal();

        // Prepare the order
        Order order = new Order();

        order.setRestaurantId(restaurantId);
        order.setUserId(user.getUserID());

        // IMPORTANT: Save the discounted amount
        order.setTotalAmount(finalTotal);

        order.setStatus("PLACED");
        order.setPaymentMode(paymentMode);

        // Prepare order items
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems().values()) {

            OrderItem item = new OrderItem();

            item.setMenuId(cartItem.getMenu().getMenuId());
            item.setQuantity(cartItem.getQuantity());
            item.setTotalAmount(cartItem.getSubtotal());

            orderItems.add(item);
        }

        // Save the order and its items
        OrderDAOImpl orderDao = new OrderDAOImpl();

        int orderId = orderDao.placeOrder(order, orderItems);

        if (orderId > 0) {

            // Record the time when order placement succeeds
            LocalDateTime orderPlacedAt = LocalDateTime.now();

            // Store order information for the success page
            session.setAttribute("confirmedOrderId", orderId);
            session.setAttribute("confirmedOrder", order);
            session.setAttribute("confirmedRestaurantId", restaurantId);
            session.setAttribute("orderPlacedAt", orderPlacedAt);

            // Store checkout details for future order-detail use
            session.setAttribute("checkoutAddress", address.trim());
            session.setAttribute("checkoutPhone", phone);
            session.setAttribute("checkoutPaymentMode", paymentMode);

            // Clear cart only after successful order placement
            cart.clearCart();
            session.setAttribute("cart", cart);

            // Redirect to the order success servlet
            resp.sendRedirect(
                req.getContextPath() + "/order-success"
            );

        } else {

            // Keep the cart if order placement fails
            req.setAttribute(
                "error",
                "Unable to place your order. Please try again."
            );

            req.setAttribute("cart", cart);
            req.setAttribute("checkoutUser", user);

            req.getRequestDispatcher("/checkout.jsp")
               .forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        resp.sendRedirect(req.getContextPath() + "/checkout");
    }
}
