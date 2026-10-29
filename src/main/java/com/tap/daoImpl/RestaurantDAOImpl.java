package com.tap.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tap.dao.RestaurantDAO;
import com.tap.model.Restaurant;
import com.tap.util.DBConnection;

public class RestaurantDAOImpl implements RestaurantDAO {

    @Override
    public void addRestaurant(Restaurant restaurant) {

        String sql = "INSERT INTO restaurant "
                   + "(Name, address, phoneNumber, CuisineType, DeliveryTime, "
                   + "AdmineUserId, Rating, IsActive, ImagePath) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, restaurant.getName());
            ps.setString(2, restaurant.getAddress());
            ps.setString(3, restaurant.getPhoneNumber());
            ps.setString(4, restaurant.getCuisineType());
            ps.setInt(5, restaurant.getDeliveryTime());
            ps.setInt(6, restaurant.getAdminUserId());
            ps.setDouble(7, restaurant.getRating());
            ps.setBoolean(8, restaurant.isActive());
            ps.setString(9, restaurant.getImagePath());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Restaurant getRestaurantById(int restaurantId) {

        String sql = "SELECT * FROM restaurant WHERE RestaurantId = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, restaurantId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractRestaurant(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {

        List<Restaurant> restaurants = new ArrayList<>();

        String sql = "SELECT * FROM restaurant";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                restaurants.add(extractRestaurant(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return restaurants;
    }

    @Override
    public List<Restaurant> getRestaurantsByAdmin(int adminUserId) {

        List<Restaurant> restaurants = new ArrayList<>();

        String sql = "SELECT * FROM restaurant WHERE AdmineUserId = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, adminUserId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                restaurants.add(extractRestaurant(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return restaurants;
    }
    

@Override
public List<Restaurant> getRestaurantsByCategory(String category) {

    List<Restaurant> restaurants = new ArrayList<>();

    if (category == null || category.trim().isEmpty()) {
        return restaurants;
    }

    String normalized = category.trim().toLowerCase();

    // Cuisine names and menu keywords based on your database.
    String[] keywords;

    switch (normalized) {
        case "burgers":
            keywords = new String[] {"burger"};
            break;

        case "biryani":
            keywords = new String[] {"biryani"};
            break;

        case "pizza":
            keywords = new String[] {"pizza"};
            break;

        case "chinese":
            keywords = new String[] {
                "noodle", "fried rice", "manchurian", "momo"
            };
            break;

        case "desserts":
            keywords = new String[] {
                "cake", "ice cream", "gulab jamun",
                "brownie", "dessert", "sweet"
            };
            break;

        case "healthy food":
        case "healthy":
            keywords = new String[] {
                "salad", "oats", "sprouts", "healthy"
            };
            normalized = "healthy food";
            break;

        case "fast food":
            keywords = new String[] {
                "burger", "pizza", "sandwich", "fries", "wrap"
            };
            normalized = "fast food";
            break;

        default:
            keywords = new String[0];
            break;
    }

    StringBuilder sql = new StringBuilder(
        "SELECT DISTINCT r.* FROM restaurant r " +
        "WHERE LOWER(r.CuisineType) = ?"
    );

    // Add menu-item matching only for categories with keywords.
    if (keywords.length > 0) {
        sql.append(
            " OR EXISTS (" +
            "SELECT 1 FROM menu m " +
            "WHERE m.RestaurantId = r.RestaurantId " +
            "AND m.IsAvailable = 1 AND ("
        );

        for (int i = 0; i < keywords.length; i++) {
            if (i > 0) {
                sql.append(" OR ");
            }
            sql.append("LOWER(m.ItemName) LIKE ?");
        }

        sql.append("))");
    }

    sql.append(" ORDER BY r.RestaurantId");

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql.toString())) {

        int parameterIndex = 1;
        ps.setString(parameterIndex++, normalized);

        for (String keyword : keywords) {
            ps.setString(parameterIndex++, "%" + keyword + "%");
        }

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                restaurants.add(extractRestaurant(rs));
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return restaurants;
}


    @Override
    public void updateRestaurant(Restaurant restaurant) {

        String sql = "UPDATE restaurant SET "
                   + "Name=?, address=?, phoneNumber=?, CuisineType=?, "
                   + "DeliveryTime=?, AdmineUserId=?, Rating=?, "
                   + "IsActive=?, ImagePath=? "
                   + "WHERE RestaurantId=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, restaurant.getName());
            ps.setString(2, restaurant.getAddress());
            ps.setString(3, restaurant.getPhoneNumber());
            ps.setString(4, restaurant.getCuisineType());
            ps.setInt(5, restaurant.getDeliveryTime());
            ps.setInt(6, restaurant.getAdminUserId());
            ps.setDouble(7, restaurant.getRating());
            ps.setBoolean(8, restaurant.isActive());
            ps.setString(9, restaurant.getImagePath());
            ps.setInt(10, restaurant.getRestaurantId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteRestaurant(int restaurantId) {

        String sql = "DELETE FROM restaurant WHERE RestaurantId = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, restaurantId);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Restaurant extractRestaurant(ResultSet rs) throws SQLException {

        Restaurant restaurant = new Restaurant();

        restaurant.setRestaurantId(rs.getInt("RestaurantId"));
        restaurant.setName(rs.getString("Name"));
        restaurant.setAddress(rs.getString("address"));
        restaurant.setPhoneNumber(rs.getString("phoneNumber"));
        restaurant.setCuisineType(rs.getString("CuisineType"));
        restaurant.setDeliveryTime(rs.getInt("DeliveryTime"));
        restaurant.setAdminUserId(rs.getInt("AdmineUserId"));
        restaurant.setRating(rs.getDouble("Rating"));
        restaurant.setActive(rs.getBoolean("IsActive"));
        restaurant.setImagePath(rs.getString("ImagePath"));

        return restaurant;
    }
}