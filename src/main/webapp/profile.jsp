
<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.tap.model.User" %>

<%
    User profileUser = (User) request.getAttribute("profileUser");

    if (profileUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>My Profile | LUDO</title>

    <link rel="stylesheet"
      href="<%= request.getContextPath() %>/CSS/common.css?v=2">

<link rel="stylesheet"
      href="<%= request.getContextPath() %>/CSS/navbar.css?v=7">

<link rel="stylesheet"
      href="<%= request.getContextPath() %>/CSS/profile.css?v=4">
</head>

<body>

    <%@ include file="includes/navbar.jsp" %>

    <main class="profile-page">

        <section class="profile-container">

            <div class="profile-header">
                <div class="profile-avatar">👤</div>

                <h1>My Profile</h1>

                <p>Your personal information</p>
            </div>

            <div class="profile-details">

                <div class="profile-field">
                    <span class="field-label">Full Name</span>
                    <span class="field-value">
                        <%= profileUser.getName() %>
                    </span>
                </div>

                <div class="profile-field">
                    <span class="field-label">Username</span>
                    <span class="field-value">
                        <%= profileUser.getUsername() %>
                    </span>
                </div>

                <div class="profile-field">
                    <span class="field-label">Email Address</span>
                    <span class="field-value">
                        <%= profileUser.getEmail() %>
                    </span>
                </div>

                <div class="profile-field">
                    <span class="field-label">Phone Number</span>
                    <span class="field-value">
                        <%= profileUser.getPhoneNumber() %>
                    </span>
                </div>

                <div class="profile-field">
                    <span class="field-label">Delivery Address</span>
                    <span class="field-value">
                        <%= profileUser.getAddress() %>
                    </span>
                </div>

            </div>

            <div class="profile-actions">

                <a href="<%= request.getContextPath() %>/profile?action=edit"
                   class="profile-btn">
                    Edit Profile
                </a>

                <a href="<%= request.getContextPath() %>/restaurant"
                   class="profile-btn">
                    Explore Restaurants
                </a>

            </div>

        </section>

    </main>

    <%@ include file="includes/footer.jsp" %>

</body>
</html>
