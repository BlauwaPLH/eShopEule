package org.senju.eshopeule.repository.redis;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public abstract class RedisRepository<T> {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(@NotNull String key, T value) {
        redisTemplate.opsForValue().set(getPrefixKey() + key, value, getTimeToLiveInSeconds(), TimeUnit.SECONDS);
    }

    @SuppressWarnings("unchecked")
    public T getByKey(@NotNull String key) {
        return (T) redisTemplate.opsForValue().get(getPrefixKey() + key);
    }

    public void deleteByKey(@NotNull String key) {
        redisTemplate.delete(getPrefixKey() + key);
    }

    protected abstract String getPrefixKey();

    protected abstract long getTimeToLiveInSeconds();
}
