<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- ================= FOOTER ================= -->

<footer class="footer">

    <div class="footer-container">

        <!-- BRAND -->

        <div class="footer-brand">

            <div class="footer-logo">
                LUDO
            </div>

            <p>
                Delicious food, delivered with love.
            </p>

        </div>


        <!-- QUICK LINKS -->

        <div class="footer-section">

            <h3>Quick Links</h3>

            <a href="<%= request.getContextPath() %>/restaurant">
                Restaurants
            </a>

            <a href="<%= request.getContextPath() %>/cart">
                Cart
            </a>

            <a href="<%= request.getContextPath() %>/orders">
                Orders
            </a>

        </div>


        <!-- ACCOUNT -->

        <div class="footer-section">

            <h3>Account</h3>

            <a href="<%= request.getContextPath() %>/login">
                Login
            </a>

            <a href="<%= request.getContextPath() %>/profile">
                Profile
            </a>

        </div>


        <!-- CONTACT -->

        <div class="footer-section">

            <h3>Contact</h3>

            <p>
                Need help with your order?
            </p>

            <p>
                support@ludo.com
            </p>

        </div>

    </div>


    <!-- FOOTER BOTTOM -->

    <div class="footer-bottom">

        <p>
            © 2026 LUDO. All rights reserved.
        </p>

    </div>

</footer>