package com.tap.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tap.dao.MenuDAO;
import com.tap.model.Menu;
import com.tap.util.DBConnection;

public class MenuDAOImpl implements MenuDAO {

    @Override
    public void addMenu(Menu menu) {

        String sql = "INSERT INTO menu "
                   + "(RestaurantId, ItemName, Description, price, "
                   + "IsAvailable, ratings, ImagePath) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, menu.getRestaurantId());
            ps.setString(2, menu.getItemName());
            ps.setString(3, menu.getDescription());
            ps.setDouble(4, menu.getPrice());
            ps.setBoolean(5, menu.isAvailable());
            ps.setDouble(6, menu.getRatings());
            ps.setString(7, menu.getImagePath());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Menu getMenuById(int menuId) {

        String sql = "SELECT * FROM menu WHERE menu_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, menuId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractMenu(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Menu> getAllMenus() {

        List<Menu> menus = new ArrayList<>();

        String sql = "SELECT * FROM menu";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                menus.add(extractMenu(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menus;
    }

    @Override
    public List<Menu> getMenusByRestaurantId(int restaurantId) {

        List<Menu> menus = new ArrayList<>();

        String sql = "SELECT * FROM menu WHERE RestaurantId = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, restaurantId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                menus.add(extractMenu(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menus;
    }

    @Override
    public void updateMenu(Menu menu) {

        String sql = "UPDATE menu SET "
                   + "RestaurantId=?, ItemName=?, Description=?, price=?, "
                   + "IsAvailable=?, ratings=?, ImagePath=? "
                   + "WHERE menu_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, menu.getRestaurantId());
            ps.setString(2, menu.getItemName());
            ps.setString(3, menu.getDescription());
            ps.setDouble(4, menu.getPrice());
            ps.setBoolean(5, menu.isAvailable());
            ps.setDouble(6, menu.getRatings());
            ps.setString(7, menu.getImagePath());
            ps.setInt(8, menu.getMenuId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMenu(int menuId) {

        String sql = "DELETE FROM menu WHERE menu_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, menuId);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Menu extractMenu(ResultSet rs) throws SQLException {

        Menu menu = new Menu();

        menu.setMenuId(rs.getInt("menu_id"));
        menu.setRestaurantId(rs.getInt("RestaurantId"));
        menu.setItemName(rs.getString("ItemName"));
        menu.setDescription(rs.getString("Description"));
        menu.setPrice(rs.getDouble("price"));
        menu.setAvailable(rs.getBoolean("IsAvailable"));
        menu.setRatings(rs.getDouble("ratings"));
        menu.setImagePath(rs.getString("ImagePath"));

        return menu;
    }
}