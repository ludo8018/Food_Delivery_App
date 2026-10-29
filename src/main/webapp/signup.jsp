```jsp
<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Sign Up | LUDO</title>

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

                <h1>Create Your <span>Account</span></h1>

                <p>Join LUDO and discover delicious food.</p>
            </div>

            <% String error = (String) request.getAttribute("error");
               if (error != null) { %>
                <div class="login-error">
                    <%= error %>
                </div>
            <% } %>

            <form class="login-form"
                  action="<%= request.getContextPath() %>/signup"
                  method="post">

                <div class="form-group">
                    <label for="name">Full Name</label>
                    <input type="text"
                           id="name"
                           name="name"
                           placeholder="Enter your full name"
                           autocomplete="name"
                           required>
              </div>
                
                <div class="form-group">
                   <label for="username">Username</label>
                   <input type="text"
                          id="username"
                          name="username"
                          placeholder="Choose a username"
                          autocomplete="username"
                          required>
               </div>

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
                    <label for="phoneNumber">Phone Number</label>
                    <input type="tel"
                           id="phoneNumber"
                           name="phoneNumber"
                           placeholder="Enter your phone number"
                           autocomplete="tel"
                           pattern="[0-9]{10}"
                           maxlength="10"
                           required>
                </div>

                <div class="form-group">
                    <label for="address">Delivery Address</label>
                    <input type="text"
                           id="address"
                           name="address"
                           placeholder="Enter your delivery address"
                           autocomplete="street-address"
                           required>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password"
                           id="password"
                           name="password"
                           placeholder="Create a password"
                           autocomplete="new-password"
                           minlength="8"
                           required>
                </div>

                <button type="submit" class="login-btn">
                    Create Account
                </button>

            </form>

            <div class="login-footer">
                <p>Already have an account?</p>

                <a href="<%= request.getContextPath() %>/login"
                   class="login-back-link">
                    Login
                </a>

                <p class="explore-text">
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
```