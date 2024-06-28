package org.senju.eshopeule.repository;

import org.senju.eshopeule.model.user.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public final class TmpUserRepository extends RedisRepository<User> {

    private static final String prefixKey = "tmp_user:";
    private static final long timeToLiveInSeconds = 15 * 60;

    public TmpUserRepository(RedisTemplate<String, Object> redisTemplate) {
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
