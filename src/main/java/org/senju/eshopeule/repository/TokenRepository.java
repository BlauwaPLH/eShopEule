package org.senju.eshopeule.repository;

import jakarta.transaction.Transactional;
import org.senju.eshopeule.model.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository  extends JpaRepository<Token, String> {
    Optional<Token> findByToken(String token);

    @Transactional
    @Modifying
    @Query(value = "UPDATE tokens SET revoked = TRUE WHERE revoked = FALSE AND token_type = 'REFRESH_TOKEN' AND identifier = :ide", nativeQuery = true)
    void revokeRefreshTokenByIdentifier(@Param("ide") String identifier);
}
