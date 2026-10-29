package com.tap.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tap.dao.OrderItemDAO;
import com.tap.model.OrderItem;
import com.tap.util.DBConnection;

public class OrderItemDAOImpl implements OrderItemDAO {

    @Override
    public void addOrderItem(OrderItem orderItem) {

        String sql = "INSERT INTO orderitem "
                   + "(orderid, menuid, quantity, totalamount) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getMenuId());
            ps.setInt(3, orderItem.getQuantity());
            ps.setDouble(4, orderItem.getTotalAmount());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public OrderItem getOrderItemById(int orderItemId) {

        String sql = "SELECT * FROM orderitem WHERE orderitemid = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderItemId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractOrderItem(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<OrderItem> getAllOrderItems() {

        List<OrderItem> orderItems = new ArrayList<>();

        String sql = "SELECT * FROM orderitem";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                orderItems.add(extractOrderItem(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItems;
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {

        List<OrderItem> orderItems = new ArrayList<>();

        String sql = "SELECT * FROM orderitem WHERE orderid = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                orderItems.add(extractOrderItem(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItems;
    }

    @Override
    public void updateOrderItem(OrderItem orderItem) {

        String sql = "UPDATE orderitem SET "
                   + "orderid=?, menuid=?, quantity=?, totalamount=? "
                   + "WHERE orderitemid=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getMenuId());
            ps.setInt(3, orderItem.getQuantity());
            ps.setDouble(4, orderItem.getTotalAmount());
            ps.setInt(5, orderItem.getOrderItemId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteOrderItem(int orderItemId) {

        String sql = "DELETE FROM orderitem WHERE orderitemid = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderItemId);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private OrderItem extractOrderItem(ResultSet rs) throws SQLException {

        OrderItem orderItem = new OrderItem();

        orderItem.setOrderItemId(rs.getInt("orderitemid"));
        orderItem.setOrderId(rs.getInt("orderid"));
        orderItem.setMenuId(rs.getInt("menuid"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setTotalAmount(rs.getDouble("totalamount"));

        return orderItem;
    }
}