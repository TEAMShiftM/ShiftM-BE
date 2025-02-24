package com.shiftm.shiftm.infra.redis;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.BusinessException;
import com.shiftm.shiftm.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveValue(final String key, final String value) {
        final ValueOperations<String, Object> operations = redisTemplate.opsForValue();

        try {
            operations.set(key, value);
        } catch (final Exception e) {
            throw new BusinessException("Redis 저장 실패", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void saveValue(final String key, final String value, final long expirationTime) {
        final ValueOperations<String, Object> operations = redisTemplate.opsForValue();

        try {
            operations.set(key, value, expirationTime, TimeUnit.MILLISECONDS);
        } catch (final Exception e) {
            throw new BusinessException("Redis 저장 실패", ErrorCode.INTERNAL_SERVER_ERROR);
        }
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
