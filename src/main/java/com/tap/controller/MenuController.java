package com.tap.controller;

import java.io.IOException;
import java.util.List;

import com.tap.daoImpl.MenuDAOImpl;
import com.tap.model.Menu;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/menu")
public class MenuController extends HttpServlet{
		
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		 int restaurantId = Integer.parseInt(req.getParameter("restaurantId"));
		
		MenuDAOImpl menuDao= new MenuDAOImpl();
		List<Menu> menuList = menuDao.getMenusByRestaurantId(restaurantId);
		
		req.setAttribute("allMenu", menuList);
		
		RequestDispatcher rd = req.getRequestDispatcher("menu.jsp");
		rd.forward(req,resp);
	}
}
