package org.senju.eshopeule.service;

import org.senju.eshopeule.model.token.Token;

public interface InMemoryTokenService {

    void delete(String identifier);

    void save(Token token);

    void save(String key, Token token);

    String get(String identifier);
}
