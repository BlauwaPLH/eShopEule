package org.senju.eshopeule.repository.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public final class VerificationCodeRepository extends RedisRepository<String> {

    private static final String prefixKey = "verification_code:";

    @Value("${security.verify-code.expiration}")
    private long timeToLiveInSeconds;

    public VerificationCodeRepository(RedisTemplate<String, Object> redisTemplate) {
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