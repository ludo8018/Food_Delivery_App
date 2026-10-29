
<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.tap.model.Cart" %>
<%@ page import="com.tap.model.CartItem" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Locale" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>LUDO - Your Cart</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/common.css">

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/cart.css">
</head>

<body>

    <!-- Navbar -->
    <%@ include file="includes/navbar.jsp" %>

    <main class="cart-page">

        <!-- Cart Header -->
        <section class="cart-header">
            <h1>Your <span>Cart</span></h1>
            <p>Review your selected items and proceed to checkout.</p>
        </section>

        <%
            Cart cart = (Cart) request.getAttribute("cart");

            boolean hasItems = cart != null
                    && cart.getItems() != null
                    && !cart.getItems().isEmpty();

            if (hasItems) {
        %>

        <!-- Cart Container -->
        <section class="cart-container">

            <!-- Cart Items -->
            <div class="cart-items">

                <%
                    for (Map.Entry<Integer, CartItem> entry
                            : cart.getItems().entrySet()) {

                        CartItem cartItem = entry.getValue();
                %>

                <article class="cart-item">

                    <!-- Food Image -->
                    <div class="cart-image-wrapper">
                        <img
                            src="<%= cartItem.getMenu().getImagePath() %>"
                            alt="<%= cartItem.getMenu().getItemName() %>"
                            class="cart-image">
                    </div>

                    <!-- Food Details -->
                    <div class="cart-details">

                        <h2>
                            <%= cartItem.getMenu().getItemName() %>
                        </h2>

                        <p>
                            <%= cartItem.getMenu().getDescription() %>
                        </p>

                        <div class="item-price">
                            ₹<%= String.format(Locale.US, "%.2f",
                                    cartItem.getMenu().getPrice()) %>
                        </div>

                        <!-- Quantity Controls -->
                        <div class="quantity-control">

                            <!-- Decrease Quantity -->
                            <form
                                action="<%= request.getContextPath() %>/cart"
                                method="post"
                                class="quantity-form">

                                <input type="hidden"
                                       name="action"
                                       value="update">

                                <input type="hidden"
                                       name="menuId"
                                       value="<%= cartItem.getMenu().getMenuId() %>">

                                <input type="hidden"
                                       name="source"
                                       value="cart">

                                <input type="hidden"
                                       name="quantity"
                                       value="<%= cartItem.getQuantity() - 1 %>">

                                <button type="submit"
                                        class="quantity-btn"
                                        aria-label="Decrease quantity">
                                    −
                                </button>
                            </form>

                            <span class="quantity-number">
                                <%= cartItem.getQuantity() %>
                            </span>

                            <!-- Increase Quantity -->
                            <form
                                action="<%= request.getContextPath() %>/cart"
                                method="post"
                                class="quantity-form">

                                <input type="hidden"
                                       name="action"
                                       value="update">

                                <input type="hidden"
                                       name="menuId"
                                       value="<%= cartItem.getMenu().getMenuId() %>">

                                <input type="hidden"
                                       name="source"
                                       value="cart">

                                <input type="hidden"
                                       name="quantity"
                                       value="<%= cartItem.getQuantity() + 1 %>">

                                <button type="submit"
                                        class="quantity-btn"
                                        aria-label="Increase quantity">
                                    +
                                </button>
                            </form>

                        </div>
                    </div>

                    <!-- Item Subtotal -->
                    <div class="subtotal">
                        <span>Subtotal</span>

                        <strong>
                            ₹<%= String.format(Locale.US, "%.2f",
                                    cartItem.getSubtotal()) %>
                        </strong>
                    </div>

                </article>

                <%
                    }
                %>

            </div>

            <!-- Order Summary -->
            <aside class="cart-total">



                <!-- Item Total -->
                <div class="total-row">
                    <span class="total-label">Item Total</span>

                    <span class="total-price">
                        ₹<%= String.format(Locale.US, "%.2f",
                                cart.getTotal()) %>
                    </span>
                </div>

                
<!-- Offers & Coupons -->
<div class="coupon-accordion">

    <button type="button"
            class="apply-coupon-link"
            id="couponToggle"
            aria-expanded="false"
            aria-controls="couponPanel">

        <span class="coupon-icon">🎟️</span>

        <span class="coupon-link-details">
            <strong>Offers &amp; Coupons</strong>
            <small>Apply a coupon and save on your order</small>
        </span>

        <span class="coupon-arrow" id="couponArrow">⌄</span>
    </button>

    <!-- Expandable Coupon Box -->
    <div class="coupon-panel" id="couponPanel" hidden>

        <h3>Available Coupons</h3>

        <!-- WELCOME50 -->
        <div class="coupon-card">
            <div class="coupon-card-info">
                <strong>WELCOME50</strong>
                <p>Get ₹50 off your order.</p>
                <small>For eligible first-time customers.</small>
            </div>

            <form action="<%= request.getContextPath() %>/cart"
                  method="post">
                <input type="hidden" name="action" value="applyCoupon">
                <input type="hidden" name="couponCode" value="WELCOME50">
                <button type="submit" class="coupon-apply-btn">APPLY</button>
            </form>
        </div>

        <!-- LUDO10 -->
        <div class="coupon-card">
            <div class="coupon-card-info">
                <strong>LUDO10</strong>
                <p>Get 10% off your food subtotal.</p>
                <small>Valid when your subtotal is above ₹199.</small>
            </div>

            <form action="<%= request.getContextPath() %>/cart"
                  method="post">
                <input type="hidden" name="action" value="applyCoupon">
                <input type="hidden" name="couponCode" value="LUDO10">
                <button type="submit" class="coupon-apply-btn">APPLY</button>
            </form>
        </div>

        <!-- Game Reward -->
        <div class="coupon-card">
            <div class="coupon-card-info">
                <strong>LUDO Winner Reward</strong>
                <p>Earn a unique coupon worth up to ₹50.</p>
                <small>Available after a verified game win.</small>
            </div>
        </div>

        <% if (request.getAttribute("couponMessage") != null) { %>
            <p class="coupon-message">
                <%= request.getAttribute("couponMessage") %>
            </p>
        <% } %>
<!-- Applied Coupon and Remove Option -->
<% if (cart.getAppliedCoupon() != null) { %>

    <div class="applied-coupon-box">

        <div class="applied-coupon-details">
            <span class="success-icon">✓</span>

            <div>
                <strong>
                    <%= cart.getAppliedCoupon().getCouponCode() %>
                </strong>

                <p>Coupon applied successfully!</p>
            </div>
        </div>

        <form action="<%= request.getContextPath() %>/cart"
              method="post">

            <input type="hidden"
                   name="action"
                   value="removeCoupon">

            <button type="submit" class="remove-coupon-btn">
                Remove
            </button>

        </form>

    </div>

<% } %>
    </div>
</div>


                <!-- Delivery Fee -->
                <div class="total-row">
                    <span class="total-label">Delivery Fee</span>

                    <span class="total-price">FREE</span>
                </div>

                <div class="total-divider"></div>

                <!-- Grand Total -->
                <div class="total-row final-total">
                    <span class="total-label">Grand Total</span>

                    <span class="total-price">
                        ₹<%= String.format(Locale.US, "%.2f",
                        		cart.getFinalTotal()) %>
                    </span>
                </div>

                <!-- Checkout -->
                <a href="<%= request.getContextPath() %>/checkout"
                   class="checkout-btn">

                    Proceed to Checkout
                    <span>→</span>
                </a>

                <p class="cart-note">
                    Delicious food is just a few steps away!
                </p>

            </aside>

        </section>

        <%
            } else {
        %>

        <!-- Empty Cart -->
        <section class="empty-cart">

            <div class="empty-cart-icon">🛒</div>

            <h2>Your Cart is Empty</h2>

            <p>
                Looks like you haven't added anything to your cart yet.
            </p>

            <a href="<%= request.getContextPath() %>/restaurant"
               class="restaurant-btn">

                Explore Restaurants
                <span>→</span>
            </a>

        </section>

        <%
            }
        %>

    </main>

    <!-- Footer -->
    <%@ include file="includes/footer.jsp" %>

</body>

<script>
    const couponToggle = document.getElementById("couponToggle");
    const couponPanel = document.getElementById("couponPanel");

    couponToggle.addEventListener("click", function () {
        const isExpanded =
            couponToggle.getAttribute("aria-expanded") === "true";

        couponToggle.setAttribute("aria-expanded", String(!isExpanded));
        couponPanel.hidden = isExpanded;
    });
</script>

</html>
