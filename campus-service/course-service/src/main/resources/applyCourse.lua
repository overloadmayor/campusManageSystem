local key=ARGV[1]
local res = redis.call('get', key)
redis.call('del',key)
return res
