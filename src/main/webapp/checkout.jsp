
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.tap.model.Cart" %>
<%@ page import="com.tap.model.CartItem" %>
<%@ page import="com.tap.model.User" %>
<%@ page import="java.util.Map" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>LUDO - Checkout</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/common.css?v=4">

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/checkout.css?v=4">
          
    
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/navbar.css">
          
</head>

<body>

<%@ include file="includes/navbar.jsp" %>

<main class="checkout-page">

    <section class="checkout-header">
        <h1>Review Your <span>Order</span></h1>
        <p>Check your items, delivery address and payment method before placing your order.</p>
    </section>

    <%
        Cart cart = (Cart) request.getAttribute("cart");
        User user = (User) session.getAttribute("user");
        String error = (String) request.getAttribute("error");

        if (cart != null && cart.getItems() != null
                && !cart.getItems().isEmpty()) {
    %>

    <% if (error != null) { %>
        <div class="checkout-error"><%= error %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/confirmorder"
          method="post"
          class="checkout-form">

        <section class="checkout-container">

            <!-- LEFT SIDE -->

            <div class="checkout-items">

                <div class="checkout-section-title">
                    <h2>Your Items</h2>
                    <span><%= cart.getItems().size() %> Items</span>
                </div>

                <% for (Map.Entry<Integer, CartItem> entry
                        : cart.getItems().entrySet()) {
                    CartItem cartItem = entry.getValue();
                %>

                <article class="checkout-item">

                    <div class="checkout-image-wrapper">
                        <img
                            src="<%= cartItem.getMenu().getImagePath() %>"
                            alt="<%= cartItem.getMenu().getItemName() %>"
                            class="checkout-image">
                    </div>

                    <div class="checkout-item-details">
                        <h3><%= cartItem.getMenu().getItemName() %></h3>
                        <p>
                            ₹<%= cartItem.getMenu().getPrice() %>
                            × <%= cartItem.getQuantity() %>
                        </p>
                    </div>

                    <div class="checkout-item-price">
                        ₹<%= cartItem.getSubtotal() %>
                    </div>

                </article>

                <% } %>

                <!-- DELIVERY DETAILS -->

                <div class="checkout-address">

                    <h2>Delivery Address</h2>

                    <% if (user != null) { %>

                    <div class="checkout-form-group">

                        <label for="deliveryAddress">
                            Full Delivery Address
                        </label>

                        <textarea
                            id="deliveryAddress"
                            name="deliveryAddress"
                            rows="4"
                            maxlength="500"
                            required
                            placeholder="Enter your complete delivery address"><%= user.getAddress() != null ? user.getAddress() : "" %></textarea>

                        <button type="button"
                                id="locationButton"
                                class="location-button">
                            📍 Use My Current Location
                        </button>

                        <p id="locationStatus"
                           class="location-status"
                           aria-live="polite"></p>

                        <input type="hidden"
                               id="latitude"
                               name="latitude">

                        <input type="hidden"
                               id="longitude"
                               name="longitude">

                    </div>

                    <div class="checkout-form-group">

                        <label for="phoneNumber">
                            Contact Phone Number
                        </label>

                        <input
                            type="tel"
                            id="phoneNumber"
                            name="phoneNumber"
                            value="<%= user.getPhoneNumber() != null ? user.getPhoneNumber() : "" %>"
                            pattern="[0-9]{10}"
                            maxlength="10"
                            required
                            placeholder="Enter 10-digit phone number">

                    </div>

                    <!-- PAYMENT -->

                    <div class="payment-section">

                        <h2>Payment Method</h2>

                        <label class="payment-option">
                            <input type="radio"
                                   name="paymentMode"
                                   value="COD"
                                   required>

                            <span>
                                <strong>Cash on Delivery</strong>
                                <small>Pay when your order arrives.</small>
                            </span>
                        </label>

                        <label class="payment-option">
                            <input type="radio"
                                   name="paymentMode"
                                   value="ONLINE"
                                   required>

                            <span>
                                <strong>Online Payment</strong>
                                <small>Requires payment gateway integration.</small>
                            </span>
                        </label>

                    </div>

                    <% } else { %>

                    <div class="login-message">
                        <p>Please log in to continue with your order.</p>
                        <a href="<%= request.getContextPath() %>/login"
                           class="login-checkout-btn">Login</a>
                    </div>

                    <% } %>

                </div>

            </div>

            <!-- RIGHT SIDE: ORDER SUMMARY -->

            <aside class="checkout-summary">

                <div class="checkout-total">

                    <h2>Order Summary</h2>

                    <div class="checkout-total-row">
                        <span>Item Total</span>
                        <span>₹<%= cart.getTotal() %></span>
                    </div>

                    <div class="checkout-total-row">
                        <span>Delivery Fee</span>
                        <span class="free">FREE</span>
                    </div>

						<div class="checkout-total-row">
							<span>Coupon Discount</span> <span>- ₹<%=cart.getDiscountAmount()%></span>
						</div>


						<div class="checkout-divider"></div>

                    <div class="checkout-grand-total">
                        <span>Grand Total</span>
                        <strong>₹<%= cart.getFinalTotal() %></strong>
                    </div>

                    <% if (user != null) { %>

                    <button type="submit"
                            class="confirm-order-btn">
                        Confirm Order <span>→</span>
                    </button>

                    <% } else { %>

                    <a href="<%= request.getContextPath() %>/login"
                       class="confirm-order-btn">
                        Login to Continue →
                    </a>

                    <% } %>

                    <p class="checkout-note">
                        Please verify your delivery details before confirming.
                    </p>

                </div>

            </aside>

        </section>

    </form>

    <% } else { %>

    <!-- EMPTY CART -->

    <section class="empty-checkout">

        <div class="empty-checkout-icon">🛒</div>

        <h2>Your Cart is Empty</h2>

        <p>Add some delicious items before proceeding to checkout.</p>

        <a href="<%= request.getContextPath() %>/restaurant"
           class="back-restaurant-btn">
            Explore Restaurants <span>→</span>
        </a>

    </section>

    <% } %>

</main>

<%@ include file="includes/footer.jsp" %>

<script>
(function () {
    const button = document.getElementById("locationButton");

    if (!button) {
        return;
    }

    const status = document.getElementById("locationStatus");
    const addressField = document.getElementById("deliveryAddress");
    const latitudeField = document.getElementById("latitude");
    const longitudeField = document.getElementById("longitude");

    button.addEventListener("click", function () {

        if (!navigator.geolocation) {
            status.textContent =
                "Your browser does not support location detection.";
            return;
        }

        button.disabled = true;
        status.textContent = "Requesting your current location...";

        navigator.geolocation.getCurrentPosition(
            async function (position) {

                const latitude = position.coords.latitude;
                const longitude = position.coords.longitude;

                latitudeField.value = latitude;
                longitudeField.value = longitude;

                status.textContent =
                    "Location detected. Finding your address...";

                try {
                    const url =
                        "https://nominatim.openstreetmap.org/reverse" +
                        "?format=jsonv2" +
                        "&lat=" + encodeURIComponent(latitude) +
                        "&lon=" + encodeURIComponent(longitude);

                    const response = await fetch(url, {
                        headers: {
                            "Accept": "application/json"
                        }
                    });

                    if (!response.ok) {
                        throw new Error("Address lookup failed.");
                    }

                    const data = await response.json();

                    if (data.display_name) {
                        addressField.value = data.display_name;

                        status.textContent =
                            "Address found. Please verify it before ordering.";
                    } else {
                        status.textContent =
                            "Address not found. Please enter it manually.";
                    }

                } catch (error) {
                    status.textContent =
                        "Coordinates detected, but address lookup failed. " +
                        "Please enter your address manually.";
                } finally {
                    button.disabled = false;
                }
            },

            function (error) {

                if (error.code === error.PERMISSION_DENIED) {
                    status.textContent =
                        "Location permission denied. Allow location access " +
                        "in your browser settings.";
                } else if (error.code === error.POSITION_UNAVAILABLE) {
                    status.textContent =
                        "Your current location is unavailable.";
                } else if (error.code === error.TIMEOUT) {
                    status.textContent =
                        "Location request timed out. Please try again.";
                } else {
                    status.textContent =
                        "Unable to detect location. Enter your address manually.";
                }

                button.disabled = false;
            },

            {
                enableHighAccuracy: true,
                timeout: 15000,
                maximumAge: 60000
            }
        );
    });
})();
</script>

</body>
</html>
