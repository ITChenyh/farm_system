package com.cyh.elesticsearch;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "goods")
public class EsGoods {
    @Id
    private Long id;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word"),
            otherFields = @InnerField(type = FieldType.Keyword, suffix = "keyword")
    )
    private String name;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String description;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String specials;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Keyword)
    private String unit;

    @Field(type = FieldType.Integer)
    private Integer store;

    @Field(type = FieldType.Integer)
    private Integer categoryId;

    // 图片字段不参与搜索，仅存储
    @Field(type = FieldType.Object, enabled = false)
    private String img;

    // getters/setters省略
}
