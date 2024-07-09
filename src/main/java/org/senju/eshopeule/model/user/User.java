package org.senju.eshopeule.model.user;

import jakarta.persistence.*;
import lombok.*;
import org.senju.eshopeule.model.BaseEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    private String password;

    @Column(nullable = false, name = "acc_non_expired")
    private boolean isAccountNonExpired;

    @Column(nullable = false, name = "acc_non_locked")
    private boolean isAccountNonLocked;

    @Column(nullable = false, name = "credentials_non_expired")
    private boolean isCredentialsNonExpired;

    @Column(nullable = false, name = "enabled")
    private boolean isEnabled;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        return id != null && id.equals(((User) obj).getId());
    }
}
