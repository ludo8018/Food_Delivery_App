package com.tap.dao;

import java.util.List;

import com.tap.model.Menu;

public interface MenuDAO {

    void addMenu(Menu menu);

    Menu getMenuById(int menuId);

    List<Menu> getAllMenus();

    List<Menu> getMenusByRestaurantId(int restaurantId);

    void updateMenu(Menu menu);

    void deleteMenu(int menuId);
}
