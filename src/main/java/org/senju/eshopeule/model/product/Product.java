package org.senju.eshopeule.model.product;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product extends AuditingEntityListener {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    private String sku;

    private String gtin;

    @Column(nullable = false)
    private Double price;

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

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "category_id", nullable = false)
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImages;

    @OneToMany(mappedBy = "product")
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
