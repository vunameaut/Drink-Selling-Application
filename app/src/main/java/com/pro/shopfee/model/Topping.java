package com.pro.shopfee.model;

import java.io.Serializable;

public class Topping implements Serializable {

    public static final String VARIANT_ICE = "variant_ice";
    public static final String VARIANT_HOT = "variant_hot";
//    public static final String SIZE_REGULAR = "size_regular";
//    public static final String SIZE_MEDIUM = "size_medium";
//    public static final String SIZE_LARGE = "size_large";
//    public static final String SUGAR_NORMAL = "sugar_normal";
//    public static final String SUGAR_LESS = "sugar_less";
    public static final String ICE_NORMAL = "ice_normal";
    public static final String ICE_LESS = "ice_less";

    public static final String SIZE_SMALL = "S";
    public static final String SIZE_MEDIUM = "M";
    public static final String SIZE_LARGE = "L";
    public static final String SIZE_XLARGE = "XL";

    public static final String SUGAR_100 = "100%";
    public static final String SUGAR_70 = "70%";
    public static final String SUGAR_50 = "50%";
    public static final String SUGAR_30 = "30%";
    public static final String SUGAR_0 = "0%";

    private long id;
    private String name;
    private int price;
    private boolean isSelected;

    public Topping() {}

    public Topping(long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
