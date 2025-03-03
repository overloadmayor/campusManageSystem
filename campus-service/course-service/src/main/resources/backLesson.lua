if(tonumber(redis.call('get',KEYS[1]))<=0) then
    --选课人数为0
    return 1
end
if(redis.call('sismember',KEYS[2],ARGV[1])==0) then
    --该学生未选这门课
    return 2
end
redis.call('incrby',KEYS[1],-1)
redis.call('srem',KEYS[2],ARGV[1])
-- 可以退课
return 0