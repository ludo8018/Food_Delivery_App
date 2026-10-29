
package com.tap.controller;

import java.io.IOException;

import com.tap.model.Cart;
import com.tap.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/confirmorder")
public class ConfirmOrderController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        resp.sendRedirect(req.getContextPath() + "/checkout");
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Cart cart = (Cart) session.getAttribute("cart");
        User user = (User) session.getAttribute("user");

        if (cart == null || cart.getItems() == null
                || cart.getItems().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        String address = req.getParameter("deliveryAddress");
        String phone = req.getParameter("phoneNumber");
        String paymentMode = req.getParameter("paymentMode");

        if (address == null || address.trim().isEmpty()
                || phone == null || !phone.matches("[0-9]{10}")
                || (!"COD".equals(paymentMode)
                    && !"ONLINE".equals(paymentMode))) {

            req.setAttribute("error",
                    "Please enter a valid address, 10-digit phone number, and payment method.");
            req.setAttribute("cart", cart);
            req.setAttribute("checkoutUser", user);
            req.getRequestDispatcher("/checkout.jsp")
               .forward(req, resp);
            return;
        }

        session.setAttribute("checkoutAddress", address.trim());
        session.setAttribute("checkoutPhone", phone);
        session.setAttribute("checkoutPaymentMode", paymentMode);

        req.setAttribute("cart", cart);
        req.setAttribute("deliveryAddress", address.trim());
        req.setAttribute("phoneNumber", phone);
        req.setAttribute("paymentMode", paymentMode);

        req.getRequestDispatcher("/confirmorder.jsp")
           .forward(req, resp);
    }
}
