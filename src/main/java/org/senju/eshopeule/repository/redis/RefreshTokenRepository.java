package org.senju.eshopeule.repository.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public final class RefreshTokenRepository extends RedisRepository<String> {
    private static final String prefixKey = "refresh_token:";

    @Value("${security.jwt.refresh-token.expiration}")
    private long ttlInSeconds;


    public RefreshTokenRepository(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected String getPrefixKey() {
        return prefixKey;
    }

    @Override
    protected long getTimeToLiveInSeconds() {
        return this.ttlInSeconds;
    }
}
