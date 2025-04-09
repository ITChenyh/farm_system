package com.cyh.entity;

/**
 * 农产品信息
 */
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "goods")
public class Goods {
    @Id
    private Integer id;
    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word"),
            otherFields = @InnerField(type = FieldType.Keyword, suffix = "keyword")
    )
    private String name;
    @Field(type = FieldType.Object, enabled = false)
    private String img;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String description;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String specials;
    @Field(type = FieldType.Scaled_Float)
    private BigDecimal price;
    @Field(type = FieldType.Keyword)
    private String unit;
    @Field(type = FieldType.Integer)
    private Integer store;
    @Field(type = FieldType.Integer)
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
