<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.tap.model.Restaurant"%>


<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>LUDO - Restaurants</title>


    
<link rel="stylesheet"
      href="<%= request.getContextPath() %>/CSS/ludo-theme.css?v=1">

<link rel="stylesheet"
      href="<%= request.getContextPath() %>/CSS/navbar.css?v=1">

<link rel="stylesheet"
      href="<%= request.getContextPath() %>/CSS/common.css?v=1">

<link rel="stylesheet"
      href="<%= request.getContextPath() %>/CSS/restaurant.css?v=17">
      
</head>


<body>


    <!-- =====================================================
         NAVBAR
         ===================================================== -->

    <%@ include file="includes/navbar.jsp" %>
    
    
<%
    String selectedCategory =
        (String) request.getAttribute("selectedCategory");

    if (selectedCategory == null) {
        selectedCategory = "All";
    }
%>

<!-- LUDO Food Categories -->
<div class="food-categories">

    <a class="category-btn <%= "All".equalsIgnoreCase(selectedCategory) ? "active" : "" %>"
       href="<%= request.getContextPath() %>/restaurant">All</a>

    <a class="category-btn <%= "South Indian".equalsIgnoreCase(selectedCategory) ? "active" : "" %>"
       href="<%= request.getContextPath() %>/restaurant?category=South%20Indian">South Indian</a>

    <a class="category-btn <%= "Burgers".equalsIgnoreCase(selectedCategory) ? "active" : "" %>"
       href="<%= request.getContextPath() %>/restaurant?category=Burgers">Burgers</a>

    <a class="category-btn <%= "Biryani".equalsIgnoreCase(selectedCategory) ? "active" : "" %>"
       href="<%= request.getContextPath() %>/restaurant?category=Biryani">Biryani</a>

    <a class="category-btn <%= "Pizza".equalsIgnoreCase(selectedCategory) ? "active" : "" %>"
       href="<%= request.getContextPath() %>/restaurant?category=Pizza">Pizza</a>

    <a class="category-btn <%= "Chinese".equalsIgnoreCase(selectedCategory) ? "active" : "" %>"
       href="<%= request.getContextPath() %>/restaurant?category=Chinese">Chinese</a>

    <a class="category-btn <%= "North Indian".equalsIgnoreCase(selectedCategory) ? "active" : "" %>"
       href="<%= request.getContextPath() %>/restaurant?category=North%20Indian">North Indian</a>

    <a class="category-btn <%= "Desserts".equalsIgnoreCase(selectedCategory) ? "active" : "" %>"
       href="<%= request.getContextPath() %>/restaurant?category=Desserts">Desserts</a>

    <a class="category-btn <%= "Healthy Food".equalsIgnoreCase(selectedCategory) ? "active" : "" %>"
       href="<%= request.getContextPath() %>/restaurant?category=Healthy%20Food">Healthy Food</a>

    <a class="category-btn <%= "Fast Food".equalsIgnoreCase(selectedCategory) ? "active" : "" %>"
       href="<%= request.getContextPath() %>/restaurant?category=Fast%20Food">Fast Food</a>

</div>
    



    <!-- =====================================================
         MAIN CONTENT
         ===================================================== -->

    <main class="restaurant-page">


        <!-- =================================================
             PAGE HEADER
             ================================================= -->

        <section class="restaurant-hero">

            <h1>
                Discover <span>Delicious</span> Food
            </h1>

            <p>
                Explore the best restaurants and discover
                delicious food near you.
            </p>

        </section>



        <!-- =================================================
             RESTAURANT LIST
             ================================================= -->

        <%
            List<Restaurant> restaurantList =
                (List<Restaurant>) request.getAttribute("restaurants");
        %>


        <%
            if (restaurantList != null && !restaurantList.isEmpty()) {
        %>


        <section class="restaurant-container">


            <%
                for (Restaurant restaurant : restaurantList) {
            %>


            <!-- =============================================
                 RESTAURANT CARD
                 ============================================= -->

            <article class="restaurant-card">


                <!-- =========================================
                     RESTAURANT IMAGE
                     ========================================= -->

                <div class="restaurant-image-wrapper">

                    <img
                        src="<%= restaurant.getImagePath() %>"
                        alt="<%= restaurant.getName() %>"
                        class="restaurant-image"
                    >


                    <!-- RATING -->

                    <div class="restaurant-rating">

                        <span>★</span>

                        <span>
                            <%= restaurant.getRating() %>
                        </span>

                    </div>

                </div>



                <!-- =========================================
                     RESTAURANT DETAILS
                     ========================================= -->

                <div class="restaurant-content">


                    <!-- RESTAURANT NAME -->

                    <h2 class="restaurant-name">

                        <%= restaurant.getName() %>

                    </h2>



                    <!-- CUISINE -->

                    <span class="restaurant-cuisine">

                        <%= restaurant.getCuisineType() %>

                    </span>



                    <!-- ADDRESS -->

                    <div class="restaurant-address">

                        <span class="restaurant-address-icon">
                            📍
                        </span>

                        <span>
                            <%= restaurant.getAddress() %>
                        </span>

                    </div>



                    <!-- DELIVERY INFORMATION -->

                    <div class="restaurant-details">

                        <div class="delivery-time">

                            <span class="delivery-icon">
                                🕒
                            </span>

                            <span>
                                <%= restaurant.getDeliveryTime() %>
                                mins
                            </span>

                        </div>

                    </div>



                    <!-- =====================================
                         VIEW MENU
                         ===================================== -->

                    <a
               href="<%= request.getContextPath() %>/menu?restaurantId=<%= restaurant.getRestaurantId() %>"
               class="menu-btn"
                >
               <span>View Menu</span>
                <span>→</span>
                  </a>


                </div>

            </article>


            <%
                }
            %>


        </section>


        <%
            } else {
        %>


        <!-- =================================================
             EMPTY RESTAURANT STATE
             ================================================= -->

        <section class="empty-restaurant">


            <div class="empty-restaurant-icon">
                🍽
            </div>


            <h2>
                No restaurants found
            </h2>


            <p>
                Currently there are no active restaurants available.
            </p>


        </section>


        <%
            }
        %>


    </main>



    <!-- =====================================================
         FOOTER
         ===================================================== -->

    <%@ include file="includes/footer.jsp" %>


</body>

</html>