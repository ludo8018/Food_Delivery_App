
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.tap.model.Order" %>

<%
    List<Order> orders =
        (List<Order>) request.getAttribute("orders");

    Map<Integer, String> restaurantNames =
        (Map<Integer, String>) request.getAttribute("restaurantNames");

    Map<Integer, List<String>> orderMenuItems =
        (Map<Integer, List<String>>) request.getAttribute("orderMenuItems");

    SimpleDateFormat dateFormat =
        new SimpleDateFormat("dd MMM yyyy, hh:mm a");
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>My Orders | LUDO</title>

    <!-- Shared navbar styling -->
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/navbar.css?v=6">

    <!-- Orders page, background, and footer styling -->
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/orders.css?v=5">
</head>

<body>

    <!-- Shared navbar -->
    <jsp:include page="/includes/navbar.jsp" />

    <main class="orders-page">

        <header class="orders-header">
            <span class="orders-eyebrow">YOUR LUDO ACCOUNT</span>

            <h1>My Orders</h1>

            <p>
                All your food orders, together in one place.
            </p>
        </header>

        <% if (orders == null || orders.isEmpty()) { %>

            <!-- Empty order state -->
            <section class="empty-orders">

                <div class="empty-orders-icon">🍽️</div>

                <h2>No orders yet</h2>

                <p>
                    Your next favourite meal is waiting for you.
                </p>

                <a class="orders-btn"
                   href="<%= request.getContextPath() %>/restaurant">
                    Explore Restaurants
                </a>

            </section>

        <% } else { %>

            <!-- Existing orders -->
            <section class="orders-list">

                <% for (Order order : orders) {

                    String status = order.getStatus();

                    if (status == null || status.trim().isEmpty()) {
                        status = "PLACED";
                    }

                    String statusClass = status
                        .toLowerCase(Locale.ROOT)
                        .replaceAll("[^a-z0-9]+", "-");

                    Timestamp orderDate = order.getOrderDate();

                    String paymentMode = order.getPaymentMode();

                    if (paymentMode == null ||
                        paymentMode.trim().isEmpty()) {
                        paymentMode = "Not specified";
                    }

                    // Get restaurant name for this order
                    String restaurantName = null;

                    if (restaurantNames != null) {
                        restaurantName =
                            restaurantNames.get(order.getOrderId());
                    }

                    if (restaurantName == null ||
                        restaurantName.trim().isEmpty()) {
                        restaurantName = "Restaurant unavailable";
                    }

                    // Get menu item names for this order
                    List<String> itemNames = null;

                    if (orderMenuItems != null) {
                        itemNames =
                            orderMenuItems.get(order.getOrderId());
                    }
                %>

                    <article class="order-card">

                        <!-- Order heading and status -->
                        <div class="order-card-top">

                            <div class="order-identity">
                                <span class="order-label">
                                    ORDER NUMBER
                                </span>

                                <h2>
                                    #<%= order.getOrderId() %>
                                </h2>
                            </div>

                            <span class="order-status <%= statusClass %>">
                                <%= status %>
                            </span>

                        </div>

                        <div class="order-card-divider"></div>

                        <!-- Order information -->
                        <div class="order-info-grid">

                            <div class="order-info">
                                <span>Restaurant</span>

                                <strong>
                                    <%= restaurantName %>
                                </strong>
                            </div>

                            <div class="order-info">
                                <span>Order Date</span>

                                <strong>
                                    <%= orderDate != null
                                        ? dateFormat.format(orderDate)
                                        : "Date unavailable" %>
                                </strong>
                            </div>

                            <div class="order-info">
                                <span>Payment Method</span>

                                <strong>
                                    <%= paymentMode %>
                                </strong>
                            </div>

                        </div>

                        <!-- Menu items belonging to this order -->
                        <div class="order-menu-items">

                            <span class="order-menu-title">
                                Items Ordered
                            </span>

                            <% if (itemNames != null &&
                                   !itemNames.isEmpty()) { %>

                                <ul>
                                    <% for (String itemName : itemNames) {
                                        if (itemName != null) { %>

                                        <li><%= itemName %></li>

                                    <%  }
                                       } %>
                                </ul>

                            <% } else { %>

                                <p class="order-no-items">
                                    Item details unavailable
                                </p>

                            <% } %>

                        </div>

                        <!-- Total amount and shopping link -->
                        <div class="order-card-bottom">

                            <div class="order-total">
                                <span>Total Amount</span>

                                <strong>
                                    ₹<%= String.format(
                                        Locale.US,
                                        "%.2f",
                                        order.getTotalAmount()
                                    ) %>
                                </strong>
                            </div>

                            <a class="order-view-btn"
                               href="<%= request.getContextPath() %>/restaurant">
                                Continue Shopping
                                <span aria-hidden="true">→</span>
                            </a>

                        </div>

                    </article>

                <% } %>

            </section>

        <% } %>

    </main>

    <!-- Shared black footer -->
    <jsp:include page="/includes/footer.jsp" />

</body>
</html>
