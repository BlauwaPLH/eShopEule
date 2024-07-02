package org.senju.eshopeule.model.product;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "brands")
public class Brand extends AuditingEntityListener {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String slug;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Brand)) return false;
        return id != null && id.equals(((Brand) obj).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
