
package com.tap.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tap.dao.OrderDAO;
import com.tap.daoImpl.OrderDAOImpl;
import com.tap.model.Order;
import com.tap.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/orders")
public class OrdersController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final OrderDAO orderDAO = new OrderDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check whether the user is logged in
        if (session == null ||
                session.getAttribute("user") == null) {

            response.sendRedirect(
                request.getContextPath() + "/login"
            );
            return;
        }

        User user = (User) session.getAttribute("user");
        int userId = user.getUserID();

        // Fetch orders belonging to the logged-in user
        List<Order> orders = orderDAO.getOrdersByUserId(userId);

        // Store restaurant names and menu item names for each order
        Map<Integer, String> restaurantNames = new HashMap<>();
        Map<Integer, List<String>> orderMenuItems = new HashMap<>();

        for (Order order : orders) {

            int orderId = order.getOrderId();
            int restaurantId = order.getRestaurantId();

            String restaurantName =
                orderDAO.getRestaurantNameById(restaurantId);

            List<String> itemNames =
                orderDAO.getMenuItemNamesByOrderId(orderId);

            restaurantNames.put(orderId, restaurantName);
            orderMenuItems.put(orderId, itemNames);
        }

        // Send the data to orders.jsp
        request.setAttribute("orders", orders);
        request.setAttribute("restaurantNames", restaurantNames);
        request.setAttribute("orderMenuItems", orderMenuItems);

        request.getRequestDispatcher("/orders.jsp")
               .forward(request, response);
    }
}
