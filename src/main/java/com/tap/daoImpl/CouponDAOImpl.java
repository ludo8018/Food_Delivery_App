
package com.tap.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tap.dao.CouponDAO;
import com.tap.model.Coupon;
import com.tap.util.DBConnection;

public class CouponDAOImpl implements CouponDAO {

    private static final String GET_COUPON_BY_CODE =
            "SELECT coupon_id, coupon_code, coupon_type, " +
            "discount_type, discount_value, min_order_amount, " +
            "max_discount, is_active " +
            "FROM coupons WHERE coupon_code = ?";

    @Override
    public Coupon getCouponByCode(String couponCode) {

        if (couponCode == null || couponCode.trim().isEmpty()) {
            return null;
        }

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                System.err.println("Unable to connect to database.");
                return null;
            }

            try (PreparedStatement ps =
                    con.prepareStatement(GET_COUPON_BY_CODE)) {

                ps.setString(1, couponCode.trim().toUpperCase());

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        double maxDiscountValue =
                                rs.getDouble("max_discount");

                        Double maxDiscount = rs.wasNull()
                                ? null : maxDiscountValue;

                        return new Coupon(
                                rs.getInt("coupon_id"),
                                rs.getString("coupon_code"),
                                rs.getString("coupon_type"),
                                rs.getString("discount_type"),
                                rs.getDouble("discount_value"),
                                rs.getDouble("min_order_amount"),
                                maxDiscount,
                                rs.getBoolean("is_active")
                        );
                    }
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
