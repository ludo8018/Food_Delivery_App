
package com.tap.controller;

import java.io.IOException;
import java.util.Map;

import com.tap.daoImpl.MenuDAOImpl;
import com.tap.model.Cart;
import com.tap.model.CartItem;
import com.tap.model.Menu;
import com.tap.model.Coupon;
import com.tap.daoImpl.CouponDAOImpl;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cart")
public class CartController extends HttpServlet {

    // ================= HANDLE CART ACTIONS =================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        HttpSession session = req.getSession();

        // ================= ADD ITEM =================
        if ("add".equals(action)) {

            try {
                int menuId = Integer.parseInt(req.getParameter("menuId"));
                int restaurantId = Integer.parseInt(
                        req.getParameter("restaurantId"));

                MenuDAOImpl menuDao = new MenuDAOImpl();
                Menu menu = menuDao.getMenuById(menuId);

                // Validate the selected menu item against the database.
                if (menu == null || menu.getRestaurantId() != restaurantId) {
                    resp.sendRedirect(req.getContextPath() + "/restaurant");
                    return;
                }

                Cart cart = (Cart) session.getAttribute("cart");

                if (cart == null) {
                    cart = new Cart();
                }

                int currentRestaurantId = getCartRestaurantId(cart);

                // Cart contains items from another restaurant.
                if (currentRestaurantId != -1
                        && currentRestaurantId != menu.getRestaurantId()) {

                    // Keep the existing cart untouched until confirmation.
                    session.setAttribute("pendingMenuId", menuId);
                    session.setAttribute(
                            "pendingRestaurantId", menu.getRestaurantId());

                    resp.sendRedirect(
                            req.getContextPath()
                            + "/menu?restaurantId=" + menu.getRestaurantId());
                    return;
                }

                // Same restaurant, or an empty cart.
                cart.addItem(menu);
                session.setAttribute("cart", cart);

                // Clear any previous pending restaurant switch.
                clearPendingSwitch(session);

                resp.sendRedirect(
                        req.getContextPath()
                        + "/menu?restaurantId=" + menu.getRestaurantId());
                return;

            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/restaurant");
                return;
            }
        }

        // ================= CONFIRM RESTAURANT SWITCH =================
        else if ("switchRestaurant".equals(action)) {

            Object pendingMenu = session.getAttribute("pendingMenuId");
            Object pendingRestaurant =
                    session.getAttribute("pendingRestaurantId");

            if (!(pendingMenu instanceof Integer)
                    || !(pendingRestaurant instanceof Integer)) {
                resp.sendRedirect(req.getContextPath() + "/restaurant");
                return;
            }

            int menuId = (Integer) pendingMenu;
            int restaurantId = (Integer) pendingRestaurant;

            try {
                MenuDAOImpl menuDao = new MenuDAOImpl();
                Menu menu = menuDao.getMenuById(menuId);

                // Revalidate the item before replacing the cart.
                if (menu == null || menu.getRestaurantId() != restaurantId) {
                    clearPendingSwitch(session);
                    resp.sendRedirect(req.getContextPath() + "/restaurant");
                    return;
                }

                // Start afresh: old items and their coupon are removed.
                Cart newCart = new Cart();
                newCart.addItem(menu);

                session.setAttribute("cart", newCart);
                clearPendingSwitch(session);

                resp.sendRedirect(
                        req.getContextPath()
                        + "/menu?restaurantId=" + restaurantId);
                return;

            } catch (Exception e) {
                getServletContext().log(
                        "Error switching cart restaurant", e);

                session.setAttribute(
                        "couponMessage",
                        "Unable to switch restaurants. Please try again.");

                resp.sendRedirect(
                        req.getContextPath() + "/restaurant");
                return;
            }
        }

        // ================= CANCEL RESTAURANT SWITCH =================
        else if ("cancelSwitch".equals(action)) {

            Object pendingRestaurant =
                    session.getAttribute("pendingRestaurantId");

            int restaurantId = pendingRestaurant instanceof Integer
                    ? (Integer) pendingRestaurant : -1;

            // Do not modify the existing cart.
            clearPendingSwitch(session);

            if (restaurantId > 0) {
                resp.sendRedirect(
                        req.getContextPath()
                        + "/menu?restaurantId=" + restaurantId);
            } else {
                resp.sendRedirect(req.getContextPath() + "/restaurant");
            }
            return;
        }

        // ================= UPDATE QUANTITY =================
        else if ("update".equals(action)) {

            try {
                int menuId = Integer.parseInt(req.getParameter("menuId"));
                int quantity = Integer.parseInt(req.getParameter("quantity"));

                Cart cart = (Cart) session.getAttribute("cart");

                if (cart != null) {
                    cart.updateItem(menuId, quantity);
                    session.setAttribute("cart", cart);
                }

                String source = req.getParameter("source");

                if ("menu".equals(source)) {
                    int restaurantId = Integer.parseInt(
                            req.getParameter("restaurantId"));

                    resp.sendRedirect(
                            req.getContextPath()
                            + "/menu?restaurantId=" + restaurantId);
                } else {
                    resp.sendRedirect(req.getContextPath() + "/cart");
                }

            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/cart");
            }
            return;
        }

        // ================= APPLY COUPON =================
        else if ("applyCoupon".equals(action)) {

            String couponCode = req.getParameter("couponCode");
            Cart cart = (Cart) session.getAttribute("cart");

            if (cart == null || cart.getItems() == null
                    || cart.getItems().isEmpty()) {

                session.setAttribute(
                        "couponMessage",
                        "Your cart is empty. Add items before applying a coupon.");

                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }

            if (couponCode == null || couponCode.trim().isEmpty()) {

                session.setAttribute(
                        "couponMessage",
                        "Please enter or select a coupon code.");

                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }

            couponCode = couponCode.trim().toUpperCase();

            try {
                CouponDAOImpl couponDao = new CouponDAOImpl();
                Coupon coupon = couponDao.getCouponByCode(couponCode);

                if (coupon == null || !coupon.isActive()) {

                    session.setAttribute(
                            "couponMessage",
                            "Invalid or inactive coupon code.");

                } else if ("ORDER_DISCOUNT".equals(coupon.getCouponType())
                        && cart.getTotal() <= 199.00) {

                    session.setAttribute(
                            "couponMessage",
                            "LUDO10 is valid only when your food subtotal is above ₹199.");

                } else if ("WELCOME".equals(coupon.getCouponType())) {

                    session.setAttribute(
                            "couponMessage",
                            "WELCOME50 requires first-order eligibility verification.");

                } else {

                    cart.applyCoupon(coupon);
                    session.setAttribute("cart", cart);

                    session.setAttribute(
                            "couponMessage",
                            "Coupon " + couponCode + " applied successfully!");
                }

            } catch (Exception e) {
                getServletContext().log("Error applying coupon", e);

                session.setAttribute(
                        "couponMessage",
                        "Unable to apply the coupon. Please try again.");
            }

            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        // ================= REMOVE COUPON =================
        else if ("removeCoupon".equals(action)) {

            Cart cart = (Cart) session.getAttribute("cart");

            if (cart != null) {
                cart.removeCoupon();
                session.setAttribute("cart", cart);
            }

            session.setAttribute(
                    "couponMessage",
                    "Coupon removed successfully.");

            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        // ================= UNKNOWN ACTION =================
        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    // ================= FIND RESTAURANT IN CURRENT CART =================
    private int getCartRestaurantId(Cart cart) {

        if (cart == null || cart.getItems() == null
                || cart.getItems().isEmpty()) {
            return -1;
        }

        for (Map.Entry<Integer, CartItem> entry
                : cart.getItems().entrySet()) {

            CartItem cartItem = entry.getValue();

            if (cartItem != null && cartItem.getMenu() != null) {
                return cartItem.getMenu().getRestaurantId();
            }
        }

        return -1;
    }

    // ================= CLEAR PENDING SWITCH DETAILS =================
    private void clearPendingSwitch(HttpSession session) {
        session.removeAttribute("pendingMenuId");
        session.removeAttribute("pendingRestaurantId");
    }

    // ================= DISPLAY CART =================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();

        Cart cart = (Cart) session.getAttribute("cart");
        req.setAttribute("cart", cart);

        String couponMessage =
                (String) session.getAttribute("couponMessage");

        if (couponMessage != null) {
            req.setAttribute("couponMessage", couponMessage);
            session.removeAttribute("couponMessage");
        }

        RequestDispatcher rd =
                req.getRequestDispatcher("/cart.jsp");

        rd.forward(req, resp);
    }
}
