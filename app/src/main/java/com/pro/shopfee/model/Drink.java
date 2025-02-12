package com.pro.shopfee.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;

@Entity(tableName = "drink")
public class Drink implements Serializable {

    @PrimaryKey
    private long id;
    private String name;
    private String description;
    private int price;
    private String image;
    private String banner;
    private long category_id;
    private String category_name;
    private int sale;
    private boolean featured;
    @Ignore
    private HashMap<String, Rating> rating;

    private int count;
    private int totalPrice;
    private int priceOneDrink;
    private String option;
    private String variant;
    private String size;
    private String sugar;
    private String ice;
    private String toppingIds;
    private String note;

    public Drink() {}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRealPrice() {
        if (sale <= 0) {
            return price;
        }
        return price - (price * sale / 100);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public HashMap<String, Rating> getRating() {
        return rating;
    }

    public void setRating(HashMap<String, Rating> rating) {
        this.rating = rating;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getPriceOneDrink() {
        return priceOneDrink;
    }

    public void setPriceOneDrink(int priceOneDrink) {
        this.priceOneDrink = priceOneDrink;
    }

    public int getCountReviews() {
        if (rating == null || rating.isEmpty()) return 0;
        return rating.size();
    }

    public double getRate() {
        if (rating == null || rating.isEmpty()) return 0;
        double sum = 0;
        for (Rating ratingEntity : rating.values()) {
            sum += ratingEntity.getRate();
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#.#");
        formatter.setDecimalFormatSymbols(symbols);
        return Double.parseDouble(formatter.format(sum / rating.size()));
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getIce() {
        return ice;
    }

    public void setIce(String ice) {
        this.ice = ice;
    }

    public String getToppingIds() {
        return toppingIds;
    }

    public void setToppingIds(String toppingIds) {
        this.toppingIds = toppingIds;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
