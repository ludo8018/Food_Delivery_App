package com.tap.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tap.dao.UserDAO;
import com.tap.model.Order;
import com.tap.model.User;
import com.tap.util.DBConnection;

public class UserDAOImpl implements UserDAO {

    @Override
    public void addUser(User user) {

        String sql = "INSERT INTO users "
                   + "(name, Username, Password, Email, phoneNumber, Address, role) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhoneNumber());
            ps.setString(6, user.getAddress());
            ps.setString(7, user.getRole());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

//@Override
//public int addOrder(Order order) {
//
//    int generatedOrderId = -1;
//
//    String INSERT_ORDER =
//        "INSERT INTO orders "
//        + "(restaurantid, userid, totalamount, status, paymentMode, orderdate) "
//        + "VALUES (?, ?, ?, ?, ?, ?)";
//
//    try (Connection connection = DBConnection.getConnection();
//         PreparedStatement statement = connection.prepareStatement(
//             INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
//
//        statement.setInt(1, order.getRestaurantId());
//        statement.setInt(2, order.getUserId());
//        statement.setDouble(3, order.getTotalAmount());
//        statement.setString(4, order.getStatus());
//        statement.setString(5, order.getPaymentMode());
//        statement.setTimestamp(6, order.getOrderDate());
//
//        statement.executeUpdate();
//
//        try (ResultSet resultSet = statement.getGeneratedKeys()) {
//            if (resultSet.next()) {
//                generatedOrderId = resultSet.getInt(1);
//                order.setOrderId(generatedOrderId);
//            }
//        }
//
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//
//    return generatedOrderId;
//}


    @Override
    public User getUserById(int userId) {

        String sql = "SELECT * FROM users WHERE userID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractUser(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User getUserByUsername(String username) {

        String sql = "SELECT * FROM users WHERE Username = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractUser(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User getUserByEmail(String email) {

        String sql = "SELECT * FROM users WHERE Email = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractUser(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User login(String email,String password) {

        String sql = "SELECT * FROM users "
                   + "WHERE Email = ? AND Password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractUser(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void updateUser(User user) {

        String sql = "UPDATE users SET "
                   + "name=?, Username=?, Password=?, Email=?, "
                   + "phoneNumber=?, Address=?, role=? "
                   + "WHERE userID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhoneNumber());
            ps.setString(6, user.getAddress());
            ps.setString(7, user.getRole());
            ps.setInt(8, user.getUserID());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(int userId) {

        String sql = "DELETE FROM users WHERE userID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(extractUser(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    private User extractUser(ResultSet rs) throws SQLException {

        User user = new User();

        user.setUserID(rs.getInt("userID"));
        user.setName(rs.getString("name"));
        user.setUsername(rs.getString("Username"));
        user.setPassword(rs.getString("Password"));
        user.setEmail(rs.getString("Email"));
        user.setPhoneNumber(rs.getString("phoneNumber"));
        user.setAddress(rs.getString("Address"));
        user.setRole(rs.getString("role"));
        user.setCreateDate(rs.getTimestamp("CreateDate"));
        user.setLastLoginDate(rs.getTimestamp("LastLoginDate"));

        return user;
    }
}
