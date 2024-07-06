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
@Table(name = "categories")
public class Category extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    private String description;

    private String metaKeyword;

    private String metaDescription;

    private boolean isPublished;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<ProductCategory> productCategories;

    @ManyToMany
    @JoinTable(
            name = "product_attribute_category",
            joinColumns = @JoinColumn(name = "category_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "product_attribute_id", nullable = false)
    )
    private List<ProductAttribute> productAttributes;

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Category)) return false;
        return id != null && id.equals(((Category) obj).getId());
    }

}
