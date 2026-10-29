
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

    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Edit Profile | LUDO</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/common.css">

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/editprofile.css">
</head>

<body>

    <%@ include file="includes/navbar.jsp" %>

    <main class="edit-profile-page">

        <section class="edit-profile-container">

            <div class="edit-profile-header">
                <div class="profile-avatar">👤</div>

                <h1>Edit <span>Profile</span></h1>

                <p>Update your personal information</p>
            </div>

            <% if (error != null) { %>
                <div class="edit-profile-error">
                    <%= error %>
                </div>
            <% } %>

            <form class="edit-profile-form"
                  action="<%= request.getContextPath() %>/profile"
                  method="post">

                <div class="edit-form-group">
                    <label for="name">Full Name</label>

                    <input type="text"
                           id="name"
                           name="name"
                           value="<%= profileUser.getName() %>"
                           maxlength="100"
                           required>
                </div>

                <div class="edit-form-group">
                    <label for="username">Username</label>

                    <input type="text"
                           id="username"
                           name="username"
                           value="<%= profileUser.getUsername() %>"
                           maxlength="50"
                           required>
                </div>

                <div class="edit-form-group">
                    <label for="email">Email Address</label>

                    <input type="email"
                           id="email"
                           value="<%= profileUser.getEmail() %>"
                           readonly>
                </div>

                <div class="edit-form-group">
                    <label for="phoneNumber">Phone Number</label>

                    <input type="tel"
                           id="phoneNumber"
                           name="phoneNumber"
                           value="<%= profileUser.getPhoneNumber() %>"
                           pattern="[0-9]{10}"
                           maxlength="10"
                           required>
                </div>

                <div class="edit-form-group">
                    <label for="address">Delivery Address</label>

                    <textarea id="address"
                              name="address"
                              rows="3"
                              maxlength="255"
                              required><%= profileUser.getAddress() %></textarea>
                </div>

                <div class="edit-profile-actions">

                    <button type="submit" class="edit-save-btn">
                        Save Changes
                    </button>

                    <a href="<%= request.getContextPath() %>/profile"
                       class="edit-cancel-btn">
                        Cancel
                    </a>

                </div>

            </form>

        </section>

    </main>

    <%@ include file="includes/footer.jsp" %>

</body>
</html>
