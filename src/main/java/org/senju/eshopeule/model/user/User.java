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
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String username;

    private String email;

    @Column(name = "phone_number")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;
}
