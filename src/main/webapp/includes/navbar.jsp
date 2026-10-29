<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.tap.model.User"%>


<!-- =========================================================
     LUDO NAVBAR
     ========================================================= -->

<nav class="navbar">


    <!-- =====================================================
         LOGO
         ===================================================== -->

    <div class="navbar-logo">

        <a href="<%= request.getContextPath() %>/restaurant"
           aria-label="LUDO Home">

            LUDO

        </a>

    </div>



    <!-- =====================================================
         NAVIGATION LINKS
         ===================================================== -->

    <div class="navbar-links">


        <!-- RESTAURANTS -->

        <a href="<%= request.getContextPath() %>/restaurant"
           class="nav-link">

            Restaurants

        </a>



        <!-- CART -->

        <a href="<%= request.getContextPath() %>/cart"
           class="nav-link">

            Cart

        </a>



        <!-- ORDERS -->

        <a href="<%= request.getContextPath() %>/orders"
           class="nav-link">

            Orders

        </a>



        <%
            User loggedInUser =
                (User) session.getAttribute("user");

            if (loggedInUser != null) {
        %>


        <!-- =================================================
             LOGGED-IN PROFILE
             ================================================= -->

        <a href="<%= request.getContextPath() %>/profile"
           class="profile-link"
           title="My Profile"
           aria-label="My Profile">

            <span class="profile-icon">
                👤
            </span>

        </a>


        <%
            } else {
        %>


        <!-- =================================================
             LOGIN
             ================================================= -->

        <a href="<%= request.getContextPath() %>/login"
           class="login-link">

            Login

        </a>


        <%
            }
        %>


    </div>

</nav>