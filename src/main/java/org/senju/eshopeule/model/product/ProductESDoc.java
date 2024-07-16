package org.senju.eshopeule.model.product;

import jakarta.persistence.Id;
import lombok.*;
import org.senju.eshopeule.model.BaseEntity;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "products")
@Setting(settingPath = "es_config/elastic-analyzer.json")
public class ProductESDoc implements BaseEntity {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String productId;

    @Field(type = FieldType.Text, analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_search")
    private String name;

    private String slug;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Double)
    private Double discount;

    private Boolean isPublished;

    private Boolean isAllowedToOrder;

    private String imageUrl;

    @Field(type = FieldType.Keyword)
    private String brand;

    @Field(type = FieldType.Keyword)
    private List<String> categories;
}
