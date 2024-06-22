package org.senju.eshopeule.repository.redis;

import org.senju.eshopeule.model.token.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, String> {
    Optional<Token> findByIdentifier(String identifier);
    Optional<Token> findByToken(String token);
    List<Token> findAllByIdentifier(String identifier);
    void deleteByIdentifier(String identifier);
    void deleteAllByIdentifier(String identifier);
}
