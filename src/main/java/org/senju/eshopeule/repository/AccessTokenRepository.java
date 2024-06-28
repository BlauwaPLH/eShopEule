package org.senju.eshopeule.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public final class AccessTokenRepository extends RedisRepository<String> {

    private static final String prefixKey = "access_token:";

    @Value("${security.jwt.access-token.expiration}")
    private long timeToLiveInSeconds;

    public AccessTokenRepository(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected String getPrefixKey() {
        return prefixKey;
    }

    @Override
    protected long getTimeToLiveInSeconds() {
        return timeToLiveInSeconds;
    }
}
