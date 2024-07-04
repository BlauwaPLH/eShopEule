package org.senju.eshopeule.model.product;

import jakarta.persistence.*;
import lombok.*;
import org.senju.eshopeule.model.AbstractAuditEntity;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_attributes")
public class ProductAttribute extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "productAttributes")
    private List<Category> categories;

    @OneToMany(
            mappedBy = "productAttribute",
            cascade = CascadeType.REMOVE
    )
    private List<ProductAttributeValue> productAttributeValues;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ProductAttribute)) return false;
        return id != null && id.equals(((ProductAttribute) obj).getId());
    }
}
