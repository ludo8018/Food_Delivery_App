
package com.tap.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tap.dao.OrderDAO;
import com.tap.model.Order;
import com.tap.model.OrderItem;
import com.tap.util.DBConnection;

public class OrderDAOImpl implements OrderDAO {


@Override
public int addOrder(Order order) {

    int generatedOrderId = -1;

    String INSERT_ORDER =
        "INSERT INTO orders "
        + "(restaurantid, userid, totalamount, status, paymentMode, orderdate) "
        + "VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(
             INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {

        statement.setInt(1, order.getRestaurantId());
        statement.setInt(2, order.getUserId());
        statement.setDouble(3, order.getTotalAmount());
        statement.setString(4, order.getStatus());
        statement.setString(5, order.getPaymentMode());
        statement.setTimestamp(6, order.getOrderDate());

        statement.executeUpdate();

        try (ResultSet resultSet = statement.getGeneratedKeys()) {
            if (resultSet.next()) {
                generatedOrderId = resultSet.getInt(1);
                order.setOrderId(generatedOrderId);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return generatedOrderId;
}


    @Override
    public Order getOrderById(int orderId) {

        String sql = "SELECT * FROM orders WHERE orderid = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractOrder(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Order> getAllOrders() {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                orders.add(extractOrder(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders WHERE userid = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(extractOrder(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    @Override
    public List<Order> getOrdersByRestaurantId(int restaurantId) {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders WHERE restaurantid = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, restaurantId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(extractOrder(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    @Override
    public void updateOrder(Order order) {

        String sql = "UPDATE orders SET "
                   + "restaurantid = ?, userid = ?, totalamount = ?, "
                   + "status = ?, paymentMode = ? WHERE orderid = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, order.getRestaurantId());
            ps.setInt(2, order.getUserId());
            ps.setDouble(3, order.getTotalAmount());
            ps.setString(4, order.getStatus());
            ps.setString(5, order.getPaymentMode());
            ps.setInt(6, order.getOrderId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteOrder(int orderId) {

        String sql = "DELETE FROM orders WHERE orderid = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

@Override
public String getRestaurantNameById(int restaurantId) {

    String sql = "SELECT Name FROM restaurant WHERE RestaurantId = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, restaurantId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("Name");
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return "Restaurant unavailable";
}

@Override
public List<String> getMenuItemNamesByOrderId(int orderId) {

    List<String> itemNames = new ArrayList<>();

    String sql =
        "SELECT m.ItemName " +
        "FROM orderitem oi " +
        "JOIN menu m ON oi.menuid = m.menu_id " +
        "WHERE oi.orderid = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, orderId);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                itemNames.add(rs.getString("ItemName"));
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return itemNames;
}


    // Place an order and save all its items in one transaction
    @Override
    public int placeOrder(Order order, List<OrderItem> orderItems) {

        if (order == null || orderItems == null || orderItems.isEmpty()) {
            return -1;
        }

        String orderSql = "INSERT INTO orders "
                + "(restaurantid, userid, totalamount, status, paymentMode) "
                + "VALUES (?, ?, ?, ?, ?)";

        String itemSql = "INSERT INTO orderitem "
                + "(orderid, menuid, quantity, totalamount) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                int orderId;

                // 1. Insert the order
                try (PreparedStatement ps = con.prepareStatement(
                        orderSql, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, order.getRestaurantId());
                    ps.setInt(2, order.getUserId());
                    ps.setDouble(3, order.getTotalAmount());

                    String status = order.getStatus();

                    if (status == null || status.trim().isEmpty()) {
                        status = "PLACED";
                    }

                    ps.setString(4, status);
                    ps.setString(5, order.getPaymentMode());

                    int rows = ps.executeUpdate();

                    if (rows == 0) {
                        throw new SQLException("Order insertion failed.");
                    }

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "Could not retrieve generated order ID.");
                        }

                        orderId = rs.getInt(1);
                    }
                }

                // 2. Insert all order items
                try (PreparedStatement ps = con.prepareStatement(itemSql)) {

                    for (OrderItem item : orderItems) {

                        if (item == null || item.getQuantity() <= 0) {
                            throw new SQLException("Invalid order item.");
                        }

                        ps.setInt(1, orderId);
                        ps.setInt(2, item.getMenuId());
                        ps.setInt(3, item.getQuantity());
                        ps.setDouble(4, item.getTotalAmount());

                        ps.addBatch();
                    }

                    ps.executeBatch();
                }

                // 3. Commit after all inserts succeed
                con.commit();

                order.setOrderId(orderId);

                return orderId;

            } catch (SQLException | RuntimeException e) {

                con.rollback();
                e.printStackTrace();

                return -1;

            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Convert a database row into an Order object
    private Order extractOrder(ResultSet rs) throws SQLException {

        Order order = new Order();

        order.setOrderId(rs.getInt("orderid"));
        order.setRestaurantId(rs.getInt("restaurantid"));
        order.setUserId(rs.getInt("userid"));
        order.setOrderDate(rs.getTimestamp("orderdate"));
        order.setTotalAmount(rs.getDouble("totalamount"));
        order.setStatus(rs.getString("status"));
        order.setPaymentMode(rs.getString("paymentMode"));

        return order;
    }
}
