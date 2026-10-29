
package com.tap.controller;

import java.io.IOException;

import com.tap.daoImpl.UserDAOImpl;
import com.tap.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/signup")
public class SignupController extends HttpServlet {

    private UserDAOImpl userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phoneNumber = req.getParameter("phoneNumber");
        String address = req.getParameter("address");
        String password = req.getParameter("password");

        // Validate required fields
        if (name == null || email == null || phoneNumber == null
                || address == null || password == null
                || name.trim().isEmpty()
                || email.trim().isEmpty()
                || phoneNumber.trim().isEmpty()
                || address.trim().isEmpty()
                || password.isEmpty()) {

            req.setAttribute("error", "Please fill in all fields.");
            req.getRequestDispatcher("/signup.jsp").forward(req, resp);
            return;
        }

        // Validate phone number
        if (!phoneNumber.matches("[0-9]{10}")) {
            req.setAttribute("error",
                    "Please enter a valid 10-digit phone number.");
            req.getRequestDispatcher("/signup.jsp").forward(req, resp);
            return;
        }

        // Validate password length
        if (password.length() < 8) {
            req.setAttribute("error",
                    "Password must contain at least 8 characters.");
            req.getRequestDispatcher("/signup.jsp").forward(req, resp);
            return;
        }

        try {
            email = email.trim();

            // Check whether the email is already registered
            User existingUser = userDAO.getUserByEmail(email);

            if (existingUser != null) {
                req.setAttribute("error",
                        "This email is already registered. Please log in.");
                req.getRequestDispatcher("/signup.jsp").forward(req, resp);
                return;
            }

            // Create a new user
            User user = new User();

            user.setName(name.trim());
            user.setEmail(email);
            user.setUsername(email);
            user.setPassword(password);
            user.setPhoneNumber(phoneNumber.trim());
            user.setAddress(address.trim());
            user.setRole("USER");

            // Save the user to the database
            userDAO.addUser(user);

            // Redirect to login after successful registration
            resp.sendRedirect(req.getContextPath() + "/login?registered=true");

        } catch (Exception e) {
            e.printStackTrace();

            req.setAttribute("error",
                    "Registration failed. Please try again.");
            req.getRequestDispatcher("/signup.jsp").forward(req, resp);
        }
    }
}
