package org.senju.eshopeule.model.product;

import jakarta.persistence.*;
import lombok.*;
import org.senju.eshopeule.model.AbstractAuditEntity;

import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    private String sku;

    private String gtin;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private double price;

    private double discount;

    @Column(nullable = false)
    private long quantity;

    @Column(nullable = false)
    private boolean hasOptions;

    @Column(nullable = false)
    private boolean isPublished;

    @Column(nullable = false)
    private boolean isAllowedToOrder;

    private String description;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = {REMOVE, PERSIST})
    private List<ProductCategory> productCategories;

    @OneToMany(mappedBy = "product", cascade = REMOVE)
    private List<ProductImage> productImages;

    @OneToMany(mappedBy = "product", cascade = REMOVE)
    private List<ProductOption> productOptions;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product)) return false;
        return id != null && id.equals(((Product) obj).getId());
    }
}
