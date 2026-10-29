
package com.tap.controller;

import java.io.IOException;
import java.util.List;

import com.tap.daoImpl.RestaurantDAOImpl;
import com.tap.model.Restaurant;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/restaurant")
public class RestaurantController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final RestaurantDAOImpl restaurantDao =
            new RestaurantDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        String category = req.getParameter("category");

        if (category == null || category.trim().isEmpty()) {
            category = "All";
        }

        List<Restaurant> restaurants;

        if ("All".equalsIgnoreCase(category)) {
            restaurants = restaurantDao.getAllRestaurants();
        } else {
            restaurants =
                    restaurantDao.getRestaurantsByCategory(category);
        }

        req.setAttribute("restaurants", restaurants);
        req.setAttribute("selectedCategory", category);

        RequestDispatcher dispatcher =
                req.getRequestDispatcher("/restaurant.jsp");

        dispatcher.forward(req, resp);
    }
}
