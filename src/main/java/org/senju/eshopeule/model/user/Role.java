package org.senju.eshopeule.model.user;

import jakarta.persistence.*;
import lombok.*;
import org.senju.eshopeule.model.BaseEntity;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "role")
    private List<User> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions;

    public Role(String name) {
        this.name = name;
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Role(String name, Collection<Permission> permissions) {
        this.name = name;
        this.permissions = (List<Permission>) permissions;
    }

    public Role(String name, String description, Collection<Permission> permissions) {
        this.name = name;
        this.description = description;
        this.permissions = (List<Permission>) permissions;
    }
}
