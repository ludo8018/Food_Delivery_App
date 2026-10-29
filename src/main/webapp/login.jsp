
<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Login | LUDO</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/common.css">

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/login.css">
</head>

<body>

    <div class="login-page">

        <div class="login-container">

            <div class="login-header">
                <a href="<%= request.getContextPath() %>/restaurant"
                   class="login-logo">LUDO</a>

                <h1>Welcome <span>Back!</span></h1>

                <p>Login to continue your food journey.</p>
            </div>

            <% String error = (String) request.getAttribute("error");
               if (error != null) { %>

                <div class="login-error">
                    <%= error %>
                </div>

            <% } %>

            <form class="login-form"
                  action="<%= request.getContextPath() %>/login"
                  method="post">

                <div class="form-group">
                    <label for="email">Email Address</label>

                    <input type="email"
                           id="email"
                           name="email"
                           placeholder="Enter your email address"
                           autocomplete="email"
                           required>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>

                    <input type="password"
                           id="password"
                           name="password"
                           placeholder="Enter your password"
                           autocomplete="current-password"
                           required>
                </div>

                <button type="submit" class="login-btn">
                    Login
                </button>

            </form>

            
         <div class="login-footer">
        <p>New to LUDO?</p>

    <a href="<%= request.getContextPath() %>/signup"
       class="signup-link">
        Sign Up
    </a>

    <p class="explore-text">
        Or
        <a href="<%= request.getContextPath() %>/restaurant"
           class="login-back-link">
            Explore Restaurants
        </a>
    </p>
      </div>


        </div>

    </div>

</body>
</html>
