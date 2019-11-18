package com.wqlm.boot.user.util.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RedisOperator extends BaseRedisOperator {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**************************************************** key 相关操作 *************************************************/

    /**
     * 实现命令：DEL key1 [key2 ...]
     * 删除任意多个 key
     *
     * @param keys
     * @return
     */
    public Long del(Collection<String> keys) {
        Set<String> keySet = new HashSet<>(keys);
        return redisTemplate.delete(keySet);
    }


    /**
     * 实现命令：UNLINK key1 [key2 ...]
     * 删除任意多个 key
     *
     * @param keys
     * @return
     */
    public Long unlink(Collection<String> keys) {
        Set<String> keySet = new HashSet<>(keys);
        return redisTemplate.unlink(keySet);
    }


    /**
     * 实现命令：EXISTS key1 [key2 ...]
     * key去重后，查看 key 是否存在，返回存在 key 的个数
     *
     * @param keys
     * @return
     */
    public Long exists(Collection<String> keys) {
        Set<String> keySet = new HashSet<>(keys);
        return redisTemplate.countExistingKeys(keySet);
    }


    /**
     * 实现命令：EXPIREAT key Unix时间戳(自1970年1月1日以来的秒数)
     * 设置key 的过期时间
     *
     * @param key
     * @param ttl 存活时间 单位秒
     * @return
     */
    public boolean expireAt(String key, int ttl) {
        Date date = new Date(System.currentTimeMillis() + ttl * 1000);
        Boolean result = redisTemplate.expireAt(key, date);
        if (null == result) {
            return false;
        }
        return result;
    }


    /**
     * 实现命令：PEXPIREAT key 自1970年1月1日以来的毫秒数
     * 设置key 的过期时间
     *
     * @param key
     * @param ttl 存活时间 单位毫秒
     * @return
     */
    public boolean pExpireAt(String key, long ttl) {
        Date date = new Date(System.currentTimeMillis() + ttl);
        Boolean result = redisTemplate.expireAt(key, date);
        if (null == result) {
            return false;
        }
        return result;
    }

    /**
     * 实现命令 : PEXPIREAT key 自1970年1月1日以来的毫秒数
     * 设置key 的过期时间
     *
     * @param key
     * @param date
     * @return
     */
    public boolean pExpireAt(String key, Date date) {
        Boolean result = redisTemplate.expireAt(key, date);
        if (null == result) {
            return false;
        }
        return result;
    }

    /**************************************************** key 相关操作 *************************************************/


    /************************************************* String 相关操作 *************************************************/

    /**
     * 实现命令 : MGET key1 [key2...]
     * key去重后，获取多个key的value
     *
     * @param keys
     * @return value
     */
    public List<Object> mGet(Collection<String> keys) {
        Set<String> keySet = new HashSet<>(keys);
        return redisTemplate.opsForValue().multiGet(keySet);
    }

    /************************************************* String 相关操作 *************************************************/


    /************************************************* Hash 相关操作 ***************************************************/

    /**
     * 实现命令 : HMGET key field1 [field2 ...]
     * 返回 多个 field 对应的值
     *
     * @param key
     * @param fields
     * @return
     */
    public List<Object> hGet(String key, Collection<Object> fields) {
        return redisTemplate.opsForHash().multiGet(key, fields);
    }


    /**
     * 实现命令 : HDEL key field [field ...]
     * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     *
     * @param key
     * @param fields
     */
    public Long hDel(String key, Collection<Object> fields) {
        Object[] objects = fields.toArray();
        return redisTemplate.opsForHash().delete(key, objects);
    }
    /************************************************* Hash 相关操作 ***************************************************/


    /************************************************* List 相关操作 ***************************************************/

    /**
     * 实现命令 : LPUSH key 元素1 [元素2 ...]
     * 在最左端推入元素
     *
     * @param key
     * @param values
     * @return 执行 LPUSH命令后，列表的长度。
     */
    public Long lPush(String key, Collection<Object> values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 实现命令 : LPUSHX key 元素
     * key 存在时在，最左端推入元素
     *
     * @param key
     * @param value
     * @return 执行 LPUSHX 命令后，列表的长度。
     */
    public Long lPushX(String key, Object value) {
        return redisTemplate.opsForList().leftPushIfPresent(key, value);
    }


    /**
     * 实现命令 : RPUSH key 元素1 [元素2 ...]
     * 在最右端推入元素
     *
     * @param key
     * @param values
     * @return 执行 RPUSH 命令后，列表的长度。
     */
    public Long rPush(String key, Collection<Object> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 实现命令 : RPUSHX key 元素
     * key 存在时，在最右端推入元素
     *
     * @param key
     * @param value
     * @return 执行 RPUSHX 命令后，列表的长度。
     */
    public Long rPushX(String key, Object value) {
        return redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /************************************************* List 相关操作 ***************************************************/


    /************************************************** SET 相关操作 ***************************************************/

    /**
     * 实现命令 : SADD key member1 [member2 ...]
     * 添加成员
     *
     * @param key
     * @param values
     * @return 添加成功的个数
     */
    public Object rPopLpush(String key, Collection<Object> values) {
        Object[] members = values.toArray();
        return redisTemplate.opsForSet().add(key, members);
    }


    /**
     * 实现命令 : SREM key member1 [member2 ...]
     * 删除指定的成员
     *
     * @param key
     * @param values
     * @return 删除成功的个数
     */
    public Long sRem(String key, Collection<Object> values) {
        Object[] members = values.toArray();
        return redisTemplate.opsForSet().remove(key, members);
    }


    /**
     * 实现命令 : SDIFF key [otherKey ...]
     * 求 key 的差集
     *
     * @param key
     * @param otherKeys
     * @return
     */
    public Set<Object> sDiff(String key, List<String> otherKeys) {
        return redisTemplate.opsForSet().difference(key, otherKeys);
    }


    /**
     * 实现命令 : SDIFFSTORE 目标key key [otherKey ...]
     * 存储 key 的差集
     *
     * @param targetKey 目标key
     * @param key
     * @param otherKeys
     * @return
     */
    public Long sDiffStore(String targetKey, String key, List<String> otherKeys) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKeys, targetKey);
    }


    /**
     * 实现命令 : SINTER key [otherKey ...]
     * 求 key 的交集
     *
     * @param key
     * @param otherKeys
     * @return
     */
    public Set<Object> sInter(String key, List<String> otherKeys) {
        return redisTemplate.opsForSet().intersect(key, otherKeys);
    }


    /**
     * 实现命令 : SINTERSTORE 目标key key [otherKey ...]
     * 存储 key 的交集
     *
     * @param targetKey 目标key
     * @param key
     * @param otherKeys
     * @return
     */
    public Long sInterStore(String targetKey, String key, List<String> otherKeys) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKeys, targetKey);
    }


    /**
     * 实现命令 : SUNION key [otherKey ...]
     * 求 key 的并集
     *
     * @param key
     * @param otherKeys
     * @return
     */
    public Set<Object> sUnion(String key, List<String> otherKeys) {
        return redisTemplate.opsForSet().union(key, otherKeys);
    }


    /**
     * 实现命令 : SUNIONSTORE 目标key key [otherKey ...]
     * 存储 key 的并集
     *
     * @param targetKey 目标key
     * @param key
     * @param otherKeys
     * @return
     */
    public Long sUnionStore(String targetKey, String key, List<String> otherKeys) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKeys, targetKey);
    }

    /************************************************** SET 相关操作 ***************************************************/


    /*********************************************** Sorted SET 相关操作 ***********************************************/

    /**
     * 实现命令 : ZADD key score1 member1 [score2 member2 ...]
     * 批量添加 成员/分数 对
     *
     * @param key
     * @param tuples
     * @return
     */
    public Long zAdd(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        return redisTemplate.opsForZSet().add(key, tuples);
    }


    /**
     * 实现命令 : ZREM key member [member ...]
     * 删除成员
     *
     * @param key
     * @param values
     * @return
     */
    public Long zRem(String key, Collection<Object> values) {
        Object[] members = values.toArray();
        return redisTemplate.opsForZSet().remove(key, members);
    }


    /**
     * 实现命令 : ZUNIONSTORE destination numkeys key1 [key2 ...]
     * 计算指定key集合与otherKey集合的并集，并保存到targetKey集合，aggregat 默认为 SUM
     *
     * @param targetKey 目标集合
     * @param key       指定集合
     * @param otherKey  其他集合
     * @return
     */
    public Long zUnionStore(String targetKey, String key, String otherKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKey, targetKey);
    }


    /**
     * 实现命令 : ZUNIONSTORE destination numkeys key1 [key2 ...]
     * 计算指定key集合与otherKey集合的并集，并保存到targetKey集合，aggregat 默认为 SUM
     *
     * @param targetKey 目标集合
     * @param key       指定集合
     * @param otherKeys 其他集合
     * @return
     */
    public Long zUnionStore(String targetKey, String key, Collection<String> otherKeys) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, targetKey);
    }


    /**
     * 实现命令 : ZUNIONSTORE destination numkeys key1 [key2 ...][AGGREGATE SUM | MIN | MAX]
     * 计算指定key集合与otherKey集合的并集，并保存到targetKey集合
     *
     * @param targetKey 目标集合
     * @param key       指定集合
     * @param otherKeys 其他集合
     * @param aggregat  SUM 将同一成员的分数相加, MIN 取同一成员中分数最小的, MAX 取同一成员中分数最大的
     * @return
     */
    public Long zUnionStore(String targetKey, String key, Collection<String> otherKeys, RedisZSetCommands.Aggregate aggregat) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, targetKey, aggregat);
    }


    /**
     * 实现命令 : ZINTERSTORE destination numkeys key1 [key2 ...]
     * 计算指定key集合与otherKey集合的交集，并保存到targetKey集合，aggregat 默认为 SUM
     *
     * @param targetKey 目标集合
     * @param key       指定集合
     * @param otherKey  其他集合
     * @return
     */
    public Long zInterStore(String targetKey, String key, String otherKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKey, targetKey);
    }


    /**
     * 实现命令 : ZINTERSTORE destination numkeys key1 [key2 ...]
     * 计算指定key集合与otherKey集合的交集，并保存到targetKey集合，aggregat 默认为 SUM
     *
     * @param targetKey 目标集合
     * @param key       指定集合
     * @param otherKeys 其他集合
     * @return
     */
    public Long zInterStore(String targetKey, String key, Collection<String> otherKeys) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, targetKey);
    }


    /**
     * 实现命令 : ZINTERSTORE destination numkeys key1 [key2 ...][AGGREGATE SUM | MIN | MAX]
     * 计算指定key集合与otherKey集合的交集，并保存到targetKey集合
     *
     * @param targetKey 目标集合
     * @param key       指定集合
     * @param otherKeys 其他集合
     * @param aggregat  SUM 将同一成员的分数相加, MIN 取同一成员中分数最小的, MAX 取同一成员中分数最大的
     * @return
     */
    public Long zInterStore(String targetKey, String key, Collection<String> otherKeys, RedisZSetCommands.Aggregate aggregat) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, targetKey, aggregat);
    }

    /*********************************************** Sorted SET 相关操作 ***********************************************/
}
