package org.senju.eshopeule.model.token;

import jakarta.persistence.*;
import lombok.*;
import org.senju.eshopeule.constant.enums.TokenType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @Column(nullable = false, name = "token_type")
    @Enumerated(value = EnumType.STRING)
    private TokenType type;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private String identifier;

    @Column(nullable = false)
    private boolean revoked;
}
