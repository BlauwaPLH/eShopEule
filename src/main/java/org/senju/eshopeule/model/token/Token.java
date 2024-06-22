package org.senju.eshopeule.model.token;

import jakarta.persistence.*;
import lombok.*;
import org.senju.eshopeule.constant.enums.TokenType;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("tokens")
public class Token {

    @Enumerated(value = EnumType.STRING)
    private TokenType type;

    private String token;

    private String identifier;
}
