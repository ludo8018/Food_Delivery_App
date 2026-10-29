
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%@ page import="com.tap.model.Order" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    Object orderIdObject = request.getAttribute("orderId");
    Order confirmedOrder = (Order) request.getAttribute("confirmedOrder");

    if (orderIdObject == null || confirmedOrder == null) {
        response.sendRedirect(request.getContextPath() + "/restaurants");
        return;
    }

    int orderId = (Integer) orderIdObject;

    LocalDateTime estimatedArrival =
        (LocalDateTime) request.getAttribute("estimatedArrival");

    Integer deliveryMinutes =
        (Integer) request.getAttribute("deliveryMinutes");

    String arrivalTime = null;

    if (estimatedArrival != null) {
        arrivalTime = estimatedArrival.format(
            DateTimeFormatter.ofPattern("hh:mm a")
        );
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Order Successful | LUDO</title>

    <link rel="stylesheet"
        href="<%= request.getContextPath() %>/CSS/navbar.css?v=6">

    <link rel="stylesheet"
        href="<%= request.getContextPath() %>/CSS/order-success.css?v=8">
</head>

<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="success-page">

        <section class="success-card">

            <!-- Success Icon -->
            <div class="success-icon">✓</div>

            <h1>Order Placed Successfully!</h1>

            <p class="success-message">
                Thank you for choosing LUDO.
                Your food order has been placed.
            </p>

            <!-- Order Number -->
            <div class="order-number">
                <span>Order Number</span>
                <strong>#<%= orderId %></strong>
            </div>

            <!-- Estimated Delivery -->
            <div class="delivery-estimate">

                <div class="delivery-estimate-icon">🛵</div>

                <div class="delivery-estimate-content">

                    <span class="delivery-label">
                        Estimated Delivery
                    </span>

                    <% if (arrivalTime != null) { %>

                        <h2><%= arrivalTime %></h2>

                        <% if (deliveryMinutes != null) { %>
                            <p>
                                Your food is expected to arrive
                                in approximately
                                <strong><%= deliveryMinutes %> minutes</strong>.
                            </p>
                        <% } else { %>
                            <p>Your estimated arrival time is shown above.</p>
                        <% } %>

                    <% } else { %>

                        <h2>Calculating...</h2>

                        <p>
                            Your estimated delivery time will appear
                            when the restaurant's delivery time is available.
                        </p>

                    <% } %>

                </div>
            </div>

            <!-- Order Details -->
            <div class="success-details">

                <div>
                    <span>Order Status</span>
                    <strong class="status">PLACED</strong>
                </div>

                <div>
                    <span>Payment Method</span>
                    <strong><%= confirmedOrder.getPaymentMode() %></strong>
                </div>

                <div>
                    <span>Total Amount</span>
                    <strong>
                        ₹<%= String.format("%.2f",
                            confirmedOrder.getTotalAmount()) %>
                    </strong>
                </div>

            </div>

            <p class="success-note">
                Your order details have been saved.
                Keep your order number for reference.
            </p>

            <!-- Continue Shopping -->
            <a class="success-btn"
                href="<%= request.getContextPath() %>/restaurant">
                Continue Shopping
            </a>

        </section>

    </main>
    
    

<div class="game-redirect-section">
    <h3>Order placed successfully!</h3>
    <p>🍔 Play LUDO, Win Rewards! 🎲 Win a discount coupon for your next meal!</p>

    <a href="<%= request.getContextPath() %>/games/ludo.jsp"
       class="game-redirect-btn">
        🎲 Play LUDO Food Fiesta →
    </a>
</div>

    

    <jsp:include page="/includes/footer.jsp" />

</body>
</html>
