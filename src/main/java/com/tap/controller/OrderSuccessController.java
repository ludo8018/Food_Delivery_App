
package com.tap.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import com.tap.dao.RestaurantDAO;
import com.tap.daoImpl.RestaurantDAOImpl;
import com.tap.model.Restaurant;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/order-success")
public class OrderSuccessController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // Verify that an order was placed
        if (session == null
                || session.getAttribute("confirmedOrderId") == null
                || session.getAttribute("confirmedOrder") == null
                || session.getAttribute("confirmedRestaurantId") == null) {

            resp.sendRedirect(
                req.getContextPath() + "/restaurants"
            );
            return;
        }

        int orderId = (Integer) session.getAttribute("confirmedOrderId");

        int restaurantId =
            (Integer) session.getAttribute("confirmedRestaurantId");

        LocalDateTime orderPlacedAt =
            (LocalDateTime) session.getAttribute("orderPlacedAt");

        // Fetch restaurant delivery time from the database
        RestaurantDAO restaurantDAO = new RestaurantDAOImpl();

        Restaurant restaurant =
            restaurantDAO.getRestaurantById(restaurantId);

        if (restaurant != null && orderPlacedAt != null) {

            // DeliveryTime is stored as minutes in your Restaurant model
            int deliveryMinutes = restaurant.getDeliveryTime();

            // Calculate expected arrival time
            LocalDateTime estimatedArrival =
                orderPlacedAt.plusMinutes(deliveryMinutes);

            // Make delivery information available to the JSP
            req.setAttribute("deliveryMinutes", deliveryMinutes);
            req.setAttribute("estimatedArrival", estimatedArrival);

        } else {
            // Avoid displaying an incorrect estimated time
            req.setAttribute("deliveryMinutes", null);
            req.setAttribute("estimatedArrival", null);
        }

        // Forward order details to the success page
        req.setAttribute("orderId", orderId);
        req.setAttribute(
            "confirmedOrder",
            session.getAttribute("confirmedOrder")
        );

        req.getRequestDispatcher("/order-success.jsp")
           .forward(req, resp);
    }
}
