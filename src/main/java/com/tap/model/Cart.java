
package com.tap.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {

    private Map<Integer, CartItem> items;
    private int restaurantId;

    // Coupon support
    private Coupon appliedCoupon;

    public Cart() {
        items = new LinkedHashMap<>();
        restaurantId = 0;
        appliedCoupon = null;
    }

    // Add item to cart
    public boolean addItem(Menu menu) {

        if (items.isEmpty()) {
            restaurantId = menu.getRestaurantId();
        }

        // Do not allow items from different restaurants
        if (restaurantId != menu.getRestaurantId()) {
            return false;
        }

        int menuId = menu.getMenuId();

        if (items.containsKey(menuId)) {

            CartItem cartItem = items.get(menuId);
            cartItem.setQuantity(cartItem.getQuantity() + 1);

        } else {

            CartItem cartItem = new CartItem(menu, 1);
            items.put(menuId, cartItem);
        }

        return true;
    }

    // Update quantity of an item
    public boolean updateItem(int menuId, int quantity) {

        if (!items.containsKey(menuId)) {
            return false;
        }

        if (quantity <= 0) {
            items.remove(menuId);
        } else {
            CartItem cartItem = items.get(menuId);
            cartItem.setQuantity(quantity);
        }

        if (items.isEmpty()) {
            restaurantId = 0;
            appliedCoupon = null;
        }

        return true;
    }

    // Remove item completely
    public boolean removeItem(int menuId) {

        if (items.containsKey(menuId)) {

            items.remove(menuId);

            if (items.isEmpty()) {
                restaurantId = 0;
                appliedCoupon = null;
            }

            return true;
        }

        return false;
    }

    // Clear entire cart
    public void clearCart() {
        items.clear();
        restaurantId = 0;
        appliedCoupon = null;
    }

    // Food subtotal before discount
    public double getTotal() {

        double total = 0;

        for (CartItem cartItem : items.values()) {
            total += cartItem.getSubtotal();
        }

        return Math.round(total * 100.0) / 100.0;
    }

    // Apply a coupon after server-side validation
    public void applyCoupon(Coupon coupon) {
        this.appliedCoupon = coupon;
    }

    // Remove applied coupon
    public void removeCoupon() {
        this.appliedCoupon = null;
    }

    public Coupon getAppliedCoupon() {
        return appliedCoupon;
    }

    // Calculate discount from the current subtotal
    public double getDiscountAmount() {

        if (appliedCoupon == null || !appliedCoupon.isActive()) {
            return 0.0;
        }

        double subtotal = getTotal();

        if (subtotal <= 0) {
            return 0.0;
        }

        String type = appliedCoupon.getCouponType();
        double discount;

        if ("ORDER_DISCOUNT".equals(type)) {

            // Strictly greater than ₹199
            if (subtotal <= 199.00) {
                return 0.0;
            }

            discount = subtotal
                    * appliedCoupon.getDiscountValue() / 100.0;

        } else if ("WELCOME".equals(type)
                || "GAME_REWARD".equals(type)) {

            discount = appliedCoupon.getDiscountValue();

        } else {
            return 0.0;
        }

        // Apply optional maximum discount
        Double maxDiscount = appliedCoupon.getMaxDiscount();

        if (maxDiscount != null && discount > maxDiscount) {
            discount = maxDiscount;
        }

        // Never discount more than the subtotal
        discount = Math.min(discount, subtotal);

        return Math.round(discount * 100.0) / 100.0;
    }

    // Final amount before delivery fees or other charges
    public double getFinalTotal() {
        return Math.round(
                (getTotal() - getDiscountAmount()) * 100.0
        ) / 100.0;
    }

    public Map<Integer, CartItem> getItems() {
        return items;
    }

    public void setItems(Map<Integer, CartItem> items) {
        this.items = items;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }
}
