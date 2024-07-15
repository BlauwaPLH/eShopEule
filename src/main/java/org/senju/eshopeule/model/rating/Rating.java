package org.senju.eshopeule.model.rating;

import jakarta.persistence.Id;
import lombok.*;
import org.senju.eshopeule.model.AbstractAuditDocument;
import org.senju.eshopeule.model.AbstractAuditEntity;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "rating")
public class Rating extends AbstractAuditDocument {

    @Id
    private String id;

    @Indexed
    private String productId;

    @Indexed
    private String customerId;

    private String customerName;

    private int ratingStar;

    private String content;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Rating)) return false;
        return id != null && id.equals(((Rating) obj).getId());
    }
}
