package com.cyh.entity;

/**
 * 农产品信息
 */

import java.math.BigDecimal;
public class Goods {
    private Integer id;
    private String name;
    private String img;
    private String description;
    private String specials;
    private BigDecimal price;
    private String unit;
    private Integer store;
    private Integer categoryId;
    private String categoryName;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public String getDescription() {
        return description;
    }

    public String getSpecials() {
        return specials;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }

    public Integer getStore() {
        return store;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSpecials(String specials) {
        this.specials = specials;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

}
