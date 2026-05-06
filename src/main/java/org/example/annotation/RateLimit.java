package org.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to trigger the Redis-based rate limiter.
 * FAANG recruiters look for AOP (Aspect Oriented Programming) to
 * keep cross-cutting concerns like this separate from business logic.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    int capacity() default 5;     // Maximum number of requests allowed in a burst
    int refillRate() default 1;  // How many tokens are added back per second
}