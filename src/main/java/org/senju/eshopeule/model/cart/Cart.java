package org.senju.eshopeule.model.cart;

import jakarta.persistence.*;
import lombok.*;
import org.senju.eshopeule.model.AbstractAuditEntity;
import org.senju.eshopeule.model.user.Customer;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class Cart extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Double total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = {PERSIST, REMOVE})
    private List<CartItem> items;

    @Enumerated(EnumType.STRING)
    private CartStatus status;


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Cart)) return false;
        return id != null && id.equals(((Cart) obj).getId());
    }
}
