package org.senju.eshopeule.model.product;

import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.C;
import org.senju.eshopeule.dto.response.BaseResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_attribute_values")
public class ProductAttributeValue implements BaseResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "product_option_id", nullable = false)
    private ProductOption productOption;

    @ManyToOne
    @JoinColumn(name = "product_attribute_id", nullable = false)
    private ProductAttribute productAttribute;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ProductAttributeValue)) return false;
        return id != null && id.equals(((ProductAttributeValue) obj).getId());
    }
}
