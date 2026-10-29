
package com.tap.dao;

import java.util.List;

import com.tap.model.Order;
import com.tap.model.OrderItem;

public interface OrderDAO {

    int addOrder(Order order);

    Order getOrderById(int orderId);

    List<Order> getAllOrders();

    List<Order> getOrdersByUserId(int userId);

    List<Order> getOrdersByRestaurantId(int restaurantId);

    void updateOrder(Order order);

    void deleteOrder(int orderId);

    // Place an order with its associated order items
    int placeOrder(Order order, List<OrderItem> orderItems);

    // Get restaurant name using restaurant ID
    String getRestaurantNameById(int restaurantId);

    // Get menu item names for a particular order
    List<String> getMenuItemNamesByOrderId(int orderId);
}
