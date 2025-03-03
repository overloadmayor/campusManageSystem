if(tonumber(redis.call('get',KEYS[1]))>=tonumber(ARGV[1])) then
    --选课人数已满
    return 1
end
if(redis.call('sismember',KEYS[2],ARGV[2])==1) then
    --重复选课
    return 2
end
--local expireTime=tonumber(ARGV[3])
redis.call('incrby',KEYS[1],1)
redis.call('sadd',KEYS[2],ARGV[2])
--redis.call('expire',KEYS[2],expireTime)
-- 可以选课
return 0