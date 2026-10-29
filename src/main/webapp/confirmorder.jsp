
<%@ page import="com.tap.model.Cart" %>
<%@ page import="com.tap.model.CartItem" %>
<%@ page import="com.tap.model.Menu" %>
<%@ page import="java.util.Map" %>
<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
    
<%
    Cart cart = (Cart) request.getAttribute("cart");

    String address = (String) request.getAttribute("deliveryAddress");
    String phone = (String) request.getAttribute("phoneNumber");
    String paymentMode = (String) request.getAttribute("paymentMode");

    if (cart == null || cart.getItems() == null
            || cart.getItems().isEmpty()
            || address == null || phone == null
            || paymentMode == null) {
        response.sendRedirect(request.getContextPath() + "/checkout");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Confirm Order | LUDO</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/common.css?v=1">

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/confirmorder.css?v=1">
          
    <link rel="stylesheet"
      href="<%= request.getContextPath() %>/CSS/navbar.css">      
</head>
<body>

<jsp:include page="/includes/navbar.jsp" />

<main class="confirm-page">

    <header class="confirm-header">
        <h1>Confirm Your <span>Order</span></h1>
        <p>Please check your food items and delivery details before continuing.</p>
    </header>

    <div class="confirm-container">

        <section class="confirm-left">

            <div class="confirm-card">
                <h2>Your Food Items</h2>

                <% for (Map.Entry<Integer, CartItem> entry
                        : cart.getItems().entrySet()) {

                    CartItem item = entry.getValue();
                    Menu menu = item.getMenu();
                %>

                <div class="confirm-item">

                    <img class="confirm-item-image"
                         src="<%= menu.getImagePath() %>"
                         alt="<%= menu.getItemName() %>">

                    <div class="confirm-item-details">
                        <h3><%= menu.getItemName() %></h3>
                        <p>
                            ₹<%= String.format("%.2f", menu.getPrice()) %>
                            × <%= item.getQuantity() %>
                        </p>
                    </div>

                    <div class="confirm-price">
                        ₹<%= String.format("%.2f", item.getSubtotal()) %>
                    </div>

                </div>

                <% } %>
            </div>

            <div class="confirm-card">
                <h2>Delivery Details</h2>

                <div class="confirm-info">
                    <strong>Delivery Address</strong>
                    <p><%= address %></p>
                </div>

                <div class="confirm-info">
                    <strong>Phone Number</strong>
                    <p><%= phone %></p>
                </div>

                <div class="confirm-info">
                    <strong>Payment Method</strong>
                    <p>
                        <%= "COD".equals(paymentMode)
                                ? "Cash on Delivery"
                                : "Online Payment" %>
                    </p>
                </div>
            </div>

        </section>

        <aside class="confirm-card confirm-summary">
            <h2>Order Summary</h2>

            <div class="confirm-summary-row">
                <span>Item Subtotal</span>
                <span>₹<%= String.format("%.2f", cart.getFinalTotal()) %></span>
            </div>

            <div class="confirm-summary-row">
                <span>Delivery Fee</span>
                <span class="free-delivery">FREE</span>
            </div>

            <div class="confirm-summary-row confirm-grand-total">
                <span>Total</span>
                <strong>₹<%= String.format("%.2f", cart.getFinalTotal()) %></strong>
            </div>

            <a class="edit-btn"
               href="<%= request.getContextPath() %>/checkout">
                Edit Details
            </a>

            <form action="<%= request.getContextPath() %>/order"
                  method="post">

                <input type="hidden" name="deliveryAddress"
                       value="<%= address %>">

                <input type="hidden" name="phoneNumber"
                       value="<%= phone %>">

                <input type="hidden" name="paymentMode"
                       value="<%= paymentMode %>">

                <button type="submit" class="confirm-btn">
                    Place Order · ₹<%= String.format("%.2f", cart.getFinalTotal()) %>
                </button>
            </form>

            <p class="confirm-note">
                Please verify your order details before placing your order.
            </p>
        </aside>

    </div>
</main>

<jsp:include page="/includes/footer.jsp" />

</body>
</html>
