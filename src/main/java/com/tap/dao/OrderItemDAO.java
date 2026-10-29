package com.tap.dao;

import java.util.List;

import com.tap.model.OrderItem;

public interface OrderItemDAO {

    void addOrderItem(OrderItem orderItem);

    OrderItem getOrderItemById(int orderItemId);

    List<OrderItem> getAllOrderItems();

    List<OrderItem> getOrderItemsByOrderId(int orderId);

    void updateOrderItem(OrderItem orderItem);

    void deleteOrderItem(int orderItemId);
}
