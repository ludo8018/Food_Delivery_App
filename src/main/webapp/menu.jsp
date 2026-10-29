
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%@ page import="java.util.List" %>
<%@ page import="com.tap.model.Menu" %>
<%@ page import="com.tap.model.Cart" %>
<%@ page import="com.tap.model.CartItem" %>
<%@ page import="com.tap.model.Restaurant" %>
<%@ page import="com.tap.daoImpl.RestaurantDAOImpl" %>

<%
    // Get the selected restaurant
    String restaurantIdParam = request.getParameter("restaurantId");
    Restaurant restaurant = null;
    int restaurantId = 0;

    if (restaurantIdParam != null) {
        try {
            restaurantId = Integer.parseInt(restaurantIdParam);

            if (restaurantId > 0) {
                RestaurantDAOImpl restaurantDao = new RestaurantDAOImpl();
                restaurant = restaurantDao.getRestaurantById(restaurantId);
            }
        } catch (NumberFormatException e) {
            restaurant = null;
        }
    }

    // Get menu items provided by MenuController
    List<Menu> menuList =
        (List<Menu>) request.getAttribute("allMenu");

    // Get the cart from the session
    Cart cart = (Cart) session.getAttribute("cart");

    // Restaurant-switch popup details
    Integer pendingMenuId =
        (Integer) session.getAttribute("pendingMenuId");

    Integer pendingRestaurantId =
        (Integer) session.getAttribute("pendingRestaurantId");

    boolean showRestaurantSwitchPopup =
        pendingMenuId != null
        && pendingRestaurantId != null
        && pendingRestaurantId == restaurantId;

    // Count total quantities of items in the cart
    int cartItemCount = 0;

    if (cart != null && cart.getItems() != null) {
        for (CartItem item : cart.getItems().values()) {
            cartItemCount += item.getQuantity();
        }
    }
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        <%= restaurant != null ? restaurant.getName() : "LUDO - Menu" %>
    </title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/common.css">

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/menu.css">

    <style>
        /* LUDO background */
        body {
            background-image: url("<%= request.getContextPath() %>/images/ludo-background.png");
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            background-attachment: fixed;
        }

        .menu-page,
        .menu-hero,
        .menu-container {
            background: transparent !important;
            background-color: transparent !important;
        }

        /* Menu cards */
        .menu-card {
            background: rgba(255, 255, 255, 0.96);
            border-radius: 22px;
            overflow: hidden;
            box-shadow: 0 5px 18px rgba(0, 0, 0, 0.12);
        }

        /* Restaurant heading */
        .restaurant-menu-name {
            color: #fff;
            font-size: clamp(26px, 4vw, 38px);
            font-weight: 800;
            margin-bottom: 10px;
            text-shadow: 0 2px 8px rgba(0, 0, 0, 0.35);
        }

        .restaurant-menu-description {
            color: #fff;
            text-shadow: 0 1px 5px rgba(0, 0, 0, 0.35);
        }

        /* Persistent bottom cart bar */
        .ludo-cart-popup {
            position: fixed;
            left: 50%;
            bottom: 18px;
            transform: translateX(-50%);
            z-index: 9999;
            width: calc(100% - 32px);
            max-width: 860px;
            min-height: 68px;
            padding: 14px 18px;
            box-sizing: border-box;
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 16px;
            background: #159f68;
            color: #fff;
            border-radius: 12px;
            box-shadow: 0 8px 28px rgba(0, 0, 0, 0.24);
        }

        .popup-message {
            display: flex;
            flex-direction: column;
            gap: 5px;
            min-width: 0;
        }

        .popup-message strong {
            font-size: 15px;
            color: #fff;
        }

        .popup-message small {
            font-size: 12px;
            color: #edfff5;
        }

        .popup-view-cart {
            flex-shrink: 0;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 12px 14px;
            color: #fff !important;
            text-decoration: none;
            font-size: 13px;
            font-weight: 800;
            white-space: nowrap;
        }

        .popup-view-cart:hover {
            background: rgba(255, 255, 255, 0.15);
            border-radius: 8px;
        }

        /* Restaurant-switch overlay */
        .restaurant-switch-overlay {
            position: fixed;
            inset: 0;
            background: rgba(0, 0, 0, 0.68);
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
            box-sizing: border-box;
            z-index: 20000;
            backdrop-filter: blur(4px);
        }

        .restaurant-switch-modal {
            width: 100%;
            max-width: 440px;
            box-sizing: border-box;
            background: #fff;
            border-radius: 20px;
            padding: 30px;
            text-align: center;
            box-shadow: 0 18px 60px rgba(0, 0, 0, 0.3);
            animation: switchPopupIn 0.22s ease-out;
        }

        .restaurant-switch-icon {
            width: 66px;
            height: 66px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 16px;
            border-radius: 50%;
            background: #e7f8ef;
            font-size: 32px;
        }

        .restaurant-switch-modal h2 {
            color: #202820;
            font-size: 24px;
            margin: 0 0 12px;
        }

        .restaurant-switch-modal p {
            color: #626a63;
            line-height: 1.65;
            font-size: 14px;
            margin: 0 0 24px;
        }

        .restaurant-switch-actions {
            display: flex;
            flex-direction: column;
            gap: 11px;
        }

        .restaurant-switch-actions form {
            width: 100%;
            margin: 0;
        }

        .restaurant-switch-actions button {
            width: 100%;
            padding: 14px 16px;
            border: none;
            border-radius: 10px;
            font-size: 14px;
            font-weight: 800;
            cursor: pointer;
            transition: background 0.2s ease, transform 0.2s ease;
        }

        .restaurant-switch-actions button:hover {
            transform: translateY(-1px);
        }

        .switch-cancel-btn {
            background: #f0f2f0;
            color: #303630;
        }

        .switch-cancel-btn:hover {
            background: #e3e7e3;
        }

        .switch-confirm-btn {
            background: #159f68;
            color: #fff;
        }

        .switch-confirm-btn:hover {
            background: #107f52;
        }

        @keyframes switchPopupIn {
            from {
                opacity: 0;
                transform: translateY(10px) scale(0.97);
            }

            to {
                opacity: 1;
                transform: translateY(0) scale(1);
            }
        }

        @media (max-width: 480px) {
            .ludo-cart-popup {
                bottom: 10px;
                padding: 12px;
                gap: 8px;
            }

            .popup-message strong {
                font-size: 13px;
            }

            .popup-message small {
                font-size: 10px;
            }

            .popup-view-cart {
                padding: 10px 8px;
                font-size: 11px;
            }

            .restaurant-switch-modal {
                padding: 24px 20px;
            }

            .restaurant-switch-modal h2 {
                font-size: 21px;
            }
        }
    </style>
</head>

<body>

    <%@ include file="includes/navbar.jsp" %>

    <main class="menu-page">

        <!-- Back to restaurants -->
        <div class="back-restaurants">
            <a href="<%= request.getContextPath() %>/restaurant">
                ← Back to Restaurants
            </a>
        </div>

        <!-- Restaurant heading -->
        <section class="menu-hero">

            <h1 class="restaurant-menu-name">
                <% if (restaurant != null) { %>
                    <%= restaurant.getName() %>
                <% } else { %>
                    Explore Our Menu
                <% } %>
            </h1>

            <p class="restaurant-menu-description">
                <% if (restaurant != null) { %>
                    Choose your favourite food from
                    <%= restaurant.getName() %>.
                <% } else { %>
                    Choose your favourite food and enjoy a delicious meal.
                <% } %>
            </p>

        </section>

        <!-- Menu items -->
        <% if (menuList != null && !menuList.isEmpty()) { %>

            <section class="menu-container">

                <% for (Menu menu : menuList) {

                    boolean isInCart = false;
                    int quantity = 0;

                    if (cart != null && cart.getItems() != null) {
                        CartItem cartItem =
                            cart.getItems().get(menu.getMenuId());

                        if (cartItem != null) {
                            isInCart = true;
                            quantity = cartItem.getQuantity();
                        }
                    }
                %>

                    <article class="menu-card">

                        <div class="menu-image-wrapper">

                            <img
                                class="menu-image"
                                src="<%= menu.getImagePath() %>"
                                alt="<%= menu.getItemName() %>">

                            <span class="menu-rating">
                                ★ <%= menu.getRatings() %>
                            </span>

                        </div>

                        <div class="menu-content">

                            <h2 class="menu-name">
                                <%= menu.getItemName() %>
                            </h2>

                            <p class="menu-description">
                                <%= menu.getDescription() %>
                            </p>

                            <div class="menu-price">
                                ₹<%= String.format("%.2f", menu.getPrice()) %>
                            </div>

                            <% if (menu.isAvailable()) { %>

                                <span class="menu-availability available">
                                    ● Available
                                </span>

                                <!-- Add to cart -->
                                <% if (!isInCart) { %>

                                    <form
                                        action="<%= request.getContextPath() %>/cart"
                                        method="post"
                                        class="menu-cart-form">

                                        <input
                                            type="hidden"
                                            name="action"
                                            value="add">

                                        <input
                                            type="hidden"
                                            name="restaurantId"
                                            value="<%= menu.getRestaurantId() %>">

                                        <input
                                            type="hidden"
                                            name="menuId"
                                            value="<%= menu.getMenuId() %>">

                                        <button
                                            type="submit"
                                            class="add-cart-btn">
                                            Add to Cart <span>→</span>
                                        </button>

                                    </form>

                                <% } else { %>

                                    <!-- Quantity controls -->
                                    <div class="menu-quantity">

                                        <!-- Decrease quantity -->
                                        <form
                                            action="<%= request.getContextPath() %>/cart"
                                            method="post">

                                            <input
                                                type="hidden"
                                                name="action"
                                                value="update">

                                            <input
                                                type="hidden"
                                                name="menuId"
                                                value="<%= menu.getMenuId() %>">

                                            <input
                                                type="hidden"
                                                name="source"
                                                value="menu">

                                            <input
                                                type="hidden"
                                                name="restaurantId"
                                                value="<%= menu.getRestaurantId() %>">

                                            <input
                                                type="hidden"
                                                name="quantity"
                                                value="<%= quantity - 1 %>">

                                            <button
                                                type="submit"
                                                class="quantity-btn">−</button>

                                        </form>

                                        <span class="quantity-value">
                                            <%= quantity %>
                                        </span>

                                        <!-- Increase quantity -->
                                        <form
                                            action="<%= request.getContextPath() %>/cart"
                                            method="post">

                                            <input
                                                type="hidden"
                                                name="action"
                                                value="update">

                                            <input
                                                type="hidden"
                                                name="menuId"
                                                value="<%= menu.getMenuId() %>">

                                            <input
                                                type="hidden"
                                                name="source"
                                                value="menu">

                                            <input
                                                type="hidden"
                                                name="restaurantId"
                                                value="<%= menu.getRestaurantId() %>">

                                            <input
                                                type="hidden"
                                                name="quantity"
                                                value="<%= quantity + 1 %>">

                                            <button
                                                type="submit"
                                                class="quantity-btn">+</button>

                                        </form>

                                    </div>

                                <% } %>

                            <% } else { %>

                                <span class="menu-availability unavailable">
                                    ● Unavailable
                                </span>

                                <button
                                    type="button"
                                    class="add-cart-btn disabled"
                                    disabled>
                                    Currently Unavailable
                                </button>

                            <% } %>

                        </div>

                    </article>

                <% } %>

            </section>

        <% } else { %>

            <!-- Empty menu -->
            <section class="empty-menu">

                <div class="empty-menu-icon">🍽️</div>

                <h2>No Menu Items Found</h2>

                <p>
                    This restaurant currently has no menu items available.
                </p>

                <br>

                <a
                    href="<%= request.getContextPath() %>/restaurant"
                    class="add-cart-btn"
                    style="display: inline-flex; width: 220px;">
                    Back to Restaurants
                </a>

            </section>

        <% } %>

    </main>

    <!-- Persistent green cart bar -->
    <% if (cartItemCount > 0) { %>

        <div class="ludo-cart-popup">

            <div class="popup-message">

                <strong>
                    ✓ <%= cartItemCount %>
                    <%= cartItemCount == 1 ? "item" : "items" %> added
                </strong>

                <small>Your food is waiting!</small>

            </div>

            <a
                href="<%= request.getContextPath() %>/cart"
                class="popup-view-cart">
                VIEW CART <span>▢</span>
            </a>

        </div>

    <% } %>

    <%@ include file="includes/footer.jsp" %>

    <!-- Restaurant-switch confirmation popup -->
    <% if (showRestaurantSwitchPopup) { %>

        <div
            class="restaurant-switch-overlay"
            role="dialog"
            aria-modal="true"
            aria-labelledby="restaurantSwitchTitle">

            <div class="restaurant-switch-modal">

                <div class="restaurant-switch-icon">🛒</div>

                <h2 id="restaurantSwitchTitle">
                    Items already in cart
                </h2>

                <p>
                    Your cart contains items from another restaurant.
                    Would you like to reset your cart for adding items
                    from this restaurant?
                </p>

                <div class="restaurant-switch-actions">

                    <!-- NO: Keep the existing cart -->
                    <form
                        action="<%= request.getContextPath() %>/cart"
                        method="post">

                        <input
                            type="hidden"
                            name="action"
                            value="cancelSwitch">

                        <button
                            type="submit"
                            class="switch-cancel-btn">
                            NO, KEEP MY CART
                        </button>

                    </form>

                    <!-- YES: Start afresh -->
                    <form
                        action="<%= request.getContextPath() %>/cart"
                        method="post"
                        >

                        <input
                            type="hidden"
                            name="action"
                            value="switchRestaurant">

                        <button
                            type="submit"
                            class="switch-confirm-btn">
                            YES, START AFRESH
                        </button>

                    </form>

                </div>

            </div>

        </div>

    <% } %>

</body>
</html>
