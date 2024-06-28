package org.senju.eshopeule;

import org.junit.jupiter.api.Test;
import org.senju.eshopeule.constant.enums.TokenType;
import org.senju.eshopeule.model.token.Token;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TokenRepositoryTests {
    @MockBean
    private TokenRepository tokenRepository;


    @Test
    public void testSaveAndFindByIdentifier() {
        String id = UUID.randomUUID().toString();
        Token token = Token.builder()
                .id(id)
                .type(TokenType.REFRESH_TOKEN)
                .token("random-token")
                .identifier("superadmin")
                .build();
        tokenRepository.save(token);

        Optional<Token> retrievedToken = tokenRepository.findByToken("random-token");
        assertTrue(retrievedToken.isPresent());
        assertEquals("random-token", retrievedToken.get().getToken());

    }
}
