package org.senju.eshopeule.model.user;

import jakarta.persistence.*;
import lombok.*;
import org.senju.eshopeule.model.BaseEntity;
import org.senju.eshopeule.model.cart.Cart;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    @Column(nullable = false)
    private String lastName;

    private LocalDateTime birth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "customer")
    private List<Cart> carts;

    public Customer(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Customer)) return false;
        return id != null && id.equals(((Customer) obj).getId());
    }

    public String getFullName() {
        return String.join(" ", firstName, middleName, lastName);
    }

}
