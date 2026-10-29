package com.tap.dao;

import java.util.List;

import com.tap.model.Restaurant;

public interface RestaurantDAO {

    void addRestaurant(Restaurant restaurant);

    Restaurant getRestaurantById(int restaurantId);

    List<Restaurant> getAllRestaurants();

    List<Restaurant> getRestaurantsByAdmin(int adminUserId);
    
 // NEW: Fetch restaurants for a selected food category
    List<Restaurant> getRestaurantsByCategory(String category);


    void updateRestaurant(Restaurant restaurant);

    void deleteRestaurant(int restaurantId);
}