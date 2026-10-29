package com.tap.controller;

import java.io.IOException;


import com.tap.daoImpl.UserDAOImpl;
import com.tap.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/login.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        UserDAOImpl userDao = new UserDAOImpl();

        User user = userDao.login(email,password);

        if (user != null) {

            HttpSession session = req.getSession();

            session.setAttribute("user", user);

            resp.sendRedirect(
                    req.getContextPath() + "/restaurant"
            );

        } else {

            req.setAttribute(
                    "error",
                    "Invalid email or password"
            );

            req.getRequestDispatcher("/login.jsp")
               .forward(req, resp);
        }
    }
}