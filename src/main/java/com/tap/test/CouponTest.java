package com.tap.test;
import com.tap.daoImpl.CouponDAOImpl;
import com.tap.model.Coupon;

public class CouponTest {

    public static void main(String[] args) {

        CouponDAOImpl dao = new CouponDAOImpl();

        Coupon coupon = dao.getCouponByCode("LUDO10");

        if (coupon != null) {
            System.out.println("Coupon: " + coupon.getCouponCode());
            System.out.println("Type: " + coupon.getCouponType());
            System.out.println("Discount: "
                    + coupon.getDiscountValue());
            System.out.println("Minimum order: "
                    + coupon.getMinOrderAmount());
            System.out.println("Active: " + coupon.isActive());
        } else {
            System.out.println("Coupon not found or database error.");
        }
    }
}
