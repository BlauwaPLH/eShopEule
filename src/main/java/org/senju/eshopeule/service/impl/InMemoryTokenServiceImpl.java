package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.model.token.Token;
import org.senju.eshopeule.service.InMemoryTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class InMemoryTokenServiceImpl implements InMemoryTokenService {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryTokenService.class);

    private final Map<String, Token> tokenStored = new ConcurrentHashMap<>();

    @Override
    public void delete(String identifier) {
        this.tokenStored.remove(identifier);
    }

    @Override
    public void save(Token token) {
        this.tokenStored.put(token.getIdentifier(), token);
    }

    @Override
    public void save(String key, Token token) {
        this.tokenStored.put(key, token);
    }

    @Override
    public String get(String identifier) {
        if (this.tokenStored.containsKey(identifier)) return this.tokenStored.get(identifier).getToken();
        return null;
    }
}
