package org.senju.eshopeule.model.product;

import jakarta.persistence.Id;
import lombok.*;
import org.senju.eshopeule.model.AbstractAuditDocument;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "product_meta")
public class ProductMeta extends AbstractAuditDocument {

    @Id
    private String id;

    @Indexed
    private String productId;

    private String metaDescription;

    private Map<String, Object> attributes;

    private List<String> tags;
}
