package org.example.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.annotation.RateLimit;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.Collections;

@Aspect
@Component
public class RateLimitAspect {

    private final StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<Long> script;

    public RateLimitAspect(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.script = new DefaultRedisScript<>();
        this.script.setResultType(Long.class);
        this.script.setScriptSource(new ResourceScriptSource(new ClassPathResource("rate_limiter.lua")));
    }

    @Before("@annotation(rateLimit)")
    public void checkLimit(RateLimit rateLimit) {
        var attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String key = "rate_limit:" + attributes.getRequest().getRemoteAddr();

        Long result = redisTemplate.execute(script,
                Collections.singletonList(key),
                String.valueOf(rateLimit.capacity()),
                String.valueOf(rateLimit.refillRate()),
                String.valueOf(Instant.now().getEpochSecond())
        );

        if (result == null || result == 0) {
            throw new RuntimeException("Too many requests! Slow down.");
        }
    }
}