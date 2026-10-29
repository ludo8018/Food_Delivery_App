
package com.tap.controller;

import java.io.IOException;

import com.tap.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/profile")
public class ProfileController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req,
	                     HttpServletResponse resp)
	        throws ServletException, IOException {

	    HttpSession session = req.getSession(false);

	    if (session == null || session.getAttribute("user") == null) {
	        resp.sendRedirect(req.getContextPath() + "/login");
	        return;
	    }

	    User user = (User) session.getAttribute("user");
	    req.setAttribute("profileUser", user);

	    String action = req.getParameter("action");

	    if ("edit".equals(action)) {
	        req.getRequestDispatcher("/editprofile.jsp")
	           .forward(req, resp);
	    } else {
	        req.getRequestDispatcher("/profile.jsp")
	           .forward(req, resp);
	    }
	}
	
	@Override
	protected void doPost(HttpServletRequest req,
	                      HttpServletResponse resp)
	        throws ServletException, IOException {

	    // Read form values
	    String name = req.getParameter("name");
	    String username = req.getParameter("username");
	    String phoneNumber = req.getParameter("phoneNumber");
	    String address = req.getParameter("address");

	    // Validate and update the user in the database here
	}
	
}	











