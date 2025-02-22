package com.shiftm.shiftm.infra.redis;

import com.shiftm.shiftm.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveValue(final String key, final String value) {
        final ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);
    }

    public void saveValue(final String key, final String value, final long expirationTime) {
        final ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value, expirationTime);
    }

    public String getValue(final String key) {
        final ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        if (valueOperations.get(key) == null) {
            throw new EntityNotFoundException(key + " Not Found");
        }

        return (String) valueOperations.get(key);
    }

    public void deleteValue(final String key) {
        redisTemplate.delete(key);
    }
}
