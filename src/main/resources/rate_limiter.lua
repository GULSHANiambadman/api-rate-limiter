local key = KEYS[1]
local capacity = tonumber(ARGV[1])
local refill_rate = tonumber(ARGV[2])
local now = tonumber(ARGV[3])

local data = redis.call("HMGET", key, "tokens", "last_fill_time")
local tokens = tonumber(data[1]) or capacity
local last_fill_time = tonumber(data[2]) or now

-- Calculate new tokens
local delta = math.max(0, now - last_fill_time)
tokens = math.min(capacity, tokens + (delta * refill_rate))

if tokens >= 1 then
    tokens = tokens - 1
    redis.call("HMSET", key, "tokens", tokens, "last_fill_time", now)
    return 1 -- Success
else
    return 0 -- Rejected
end