package com.wqlm.boot.user.util.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class BaseRedisOperator {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    enum Position {
        BEFORE, AFTER
    }


    /**************************************************** key 相关操作 *************************************************/

    /**
     * 实现命令 : KEYS pattern
     * 查找所有符合 pattern 模式的 key
     * ?     匹配单个字符
     * *     匹配0到多个字符
     * [a-c] 匹配a和c
     * [ac]  匹配a到c
     * [^a]  匹配除了a以外的字符
     *
     * @param pattern redis pattern 表达式
     * @return
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }


    /**
     * 实现命令 : DEL key1 [key2 ...]
     * 删除一个或多个key
     *
     * @param keys
     * @return
     */
    public Long del(String... keys) {
        Set<String> keySet = Stream.of(keys).collect(Collectors.toSet());
        return redisTemplate.delete(keySet);
    }

    /**
     * 实现命令 : UNLINK key1 [key2 ...]
     * 删除一个或多个key
     *
     * @param keys
     * @return
     */
    public Long unlink(String... keys) {
        Set<String> keySet = Stream.of(keys).collect(Collectors.toSet());
        return redisTemplate.unlink(keySet);
    }


    /**
     * 实现命令 : EXISTS key1 [key2 ...]
     * 查看 key 是否存在，返回存在 key 的个数
     *
     * @param keys
     * @return
     */
    public Long exists(String... keys) {
        Set<String> keySet = Stream.of(keys).collect(Collectors.toSet());
        return redisTemplate.countExistingKeys(keySet);
    }


    /**
     * 实现命令 : TYPE key
     * 查看 key 的 value 的类型
     *
     * @param key
     * @return
     */
    public String type(String key) {
        DataType dataType = redisTemplate.type(key);
        if (dataType == null) {
            return "";
        }
        return dataType.code();
    }


    /**
     * 实现命令 : PERSIST key
     * 取消 key 的超时时间，持久化 key
     *
     * @param key
     * @return
     */
    public boolean persist(String key) {
        Boolean result = redisTemplate.persist(key);
        if (null == result) {
            return false;
        }
        return result;
    }


    /**
     * 实现命令 : TTL key
     * 返回给定 key 的剩余生存时间,key不存在返回 null
     * 单位: 秒
     *
     * @param key
     * @return
     */
    public Long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 实现命令 : PTTL key
     * 返回给定 key 的剩余生存时间,key不存在返回 null
     * 单位: 毫秒
     *
     * @param key
     * @return
     */
    public Long pTtl(String key) {
        return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    /**
     * 实现命令 : EXPIRE key 秒
     * 设置key 的生存时间
     * 单位 : 秒
     *
     * @param key
     * @return
     */
    public boolean expire(String key, int ttl) {
        Boolean result = redisTemplate.expire(key, ttl, TimeUnit.SECONDS);
        if (null == result) {
            return false;
        }
        return result;
    }

    /**
     * 实现命令 : PEXPIRE key 毫秒
     * 设置key 的生存时间
     * 单位 : 毫秒
     *
     * @param key
     * @return
     */
    public boolean pExpire(String key, Long ttl) {
        Boolean result = redisTemplate.expire(key, ttl, TimeUnit.MILLISECONDS);
        if (null == result) {
            return false;
        }
        return result;
    }

    /**
     * 实现命令 : EXPIREAT key Unix时间戳(自1970年1月1日以来的秒数)
     * 设置key 的过期时间
     *
     * @param key
     * @param date
     * @return
     */
    public boolean expireAt(String key, Date date) {
        Boolean result = redisTemplate.expireAt(key, date);
        if (null == result) {
            return false;
        }
        return result;
    }


    /**
     * 实现命令 : RENAME key newkey
     * 重命名key，如果newKey已经存在，则newKey的原值被覆盖
     *
     * @param oldKey
     * @param newKey
     */
    public void rename(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 实现命令 : RENAMENX key newkey
     * 安全重命名key，newKey不存在时才重命名
     *
     * @param oldKey
     * @param newKey
     * @return
     */
    public boolean renameNx(String oldKey, String newKey) {
        Boolean result = redisTemplate.renameIfAbsent(oldKey, newKey);
        if (null == result) {
            return false;
        }
        return result;
    }

    /**************************************************** key 相关操作 *************************************************/


    /************************************************* String 相关操作 *************************************************/

    /**
     * 实现命令 : SET key value
     * 添加一个持久化的 String 类型的键值对
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 实现命令 : SET key value EX 秒、 setex  key value 秒
     * 添加一个 String 类型的键值对，并设置生存时间
     *
     * @param key
     * @param value
     * @param ttl   key 的生存时间，单位:秒
     */
    public void set(String key, Object value, int ttl) {
        redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);
    }


    /**
     * 实现命令 : SET key value PX 毫秒 、 psetex key value 毫秒
     * 添加一个 String 类型的键值对，并设置生存时间
     *
     * @param key
     * @param value
     * @param ttl   key 的生存时间，单位:毫秒
     */
    public void set(String key, Object value, long ttl) {
        redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.MILLISECONDS);
    }


    /**
     * 实现命令 : SET key value [EX 秒|PX 毫秒] [NX|XX]
     * 添加一个 String 类型的键值对，
     * ttl、timeUnit 不为 null 时设置生存时间
     * keyIfExist 不为 null 时，设置 NX 或 XX 模式
     *
     * @param key
     * @param value
     * @param ttl        生存时间
     * @param timeUnit   生存时间的单位
     * @param keyIfExist true 表示 xx,key 存在时才添加. false 表示 nx，key 不存在时才添加
     */
    public boolean set(String key, Object value, Long ttl, TimeUnit timeUnit, Boolean keyIfExist) {
        Boolean result = false;

        if ((ttl == null || timeUnit == null) && (keyIfExist == null)) {
            // SET key value
            redisTemplate.opsForValue().set(key, value);
            result = true;
        }

        if (ttl != null && timeUnit != null && keyIfExist == null) {
            // SET key value [EX 秒|PX 毫秒]
            redisTemplate.opsForValue().set(key, value, ttl, timeUnit);
            result = true;
        }

        if ((ttl == null || timeUnit == null) && (keyIfExist != null) && keyIfExist) {
            // SET key value XX
            result = redisTemplate.opsForValue().setIfPresent(key, value);
        }

        if (ttl != null && timeUnit != null && keyIfExist != null && keyIfExist) {
            // SET key value [EX 秒|PX 毫秒] XX
            result = redisTemplate.opsForValue().setIfPresent(key, value, ttl, timeUnit);
        }

        if ((ttl == null || timeUnit == null) && (keyIfExist != null) && (!keyIfExist)) {
            // SET key value NX
            result = redisTemplate.opsForValue().setIfAbsent(key, value);
        }

        if (ttl != null && timeUnit != null && keyIfExist != null && (!keyIfExist)) {
            // SET key value [EX 秒|PX 毫秒] NX
            result = redisTemplate.opsForValue().setIfAbsent(key, value, ttl, timeUnit);

        }

        if (result == null) {
            return false;
        }
        return result;
    }


    /**
     * 实现命令 : MSET key1 value1 [key2 value2...]
     * 安全批量添加键值对,只要有一个 key 已存在，所有的键值对都不会插入
     *
     * @param keyValueMap
     */
    public void mSet(Map<String, Object> keyValueMap) {
        redisTemplate.opsForValue().multiSetIfAbsent(keyValueMap);
    }


    /**
     * 实现命令 : MSETNX key1 value1 [key2 value2...]
     * 批量添加键值对
     *
     * @param keyValueMap
     */
    public void mSetNx(Map<String, Object> keyValueMap) {
        redisTemplate.opsForValue().multiSet(keyValueMap);
    }


    /**
     * 实现命令 : SETRANGE key 下标 str
     * 覆盖 原始 value 的一部分，从指定的下标开始覆盖， 覆盖的长度为指定的字符串的长度。
     *
     * @param key
     * @param str    字符串
     * @param offset 开始覆盖的位置，包括开始位置，下标从0开始
     */
    public void setRange(String key, Object str, int offset) {
        redisTemplate.opsForValue().set(key, str, offset);
    }


    /**
     * 实现命令 : APPEND key value
     * 在原始 value 末尾追加字符串
     *
     * @param key
     * @param str 要追加的字符串
     */
    public void append(String key, String str) {
        redisTemplate.opsForValue().append(key, str);
    }


    /**
     * 实现命令 : GETSET key value
     * 设置 key 的 value 并返回旧 value
     *
     * @param key
     * @param value
     */
    public Object getSet(String key, Object value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }


    /**
     * 实现命令 : GET key
     * 获取一个key的value
     *
     * @param key
     * @return value
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 实现命令 : MGET key1 [key2...]
     * 获取多个key的value
     *
     * @param keys
     * @return value
     */
    public List<Object> mGet(String... keys) {
        Set<String> keySet = Stream.of(keys).collect(Collectors.toSet());
        return redisTemplate.opsForValue().multiGet(keySet);
    }


    /**
     * 实现命令 : GETRANGE key 开始下标 结束下标
     * 获取指定key的value的子串，下标从0开始，包括开始下标，也包括结束下标。
     *
     * @param key
     * @param start 开始下标
     * @param end   结束下标
     * @return
     */
    public String getRange(String key, int start, int end) {
        return redisTemplate.opsForValue().get(key, start, end);
    }


    /**
     * 实现命令 : STRLEN key
     * 获取 key 对应 value 的字符串长度
     *
     * @param key
     * @return
     */
    public Long strLen(String key) {
        return redisTemplate.opsForValue().size(key);
    }


    /**
     * 实现命令 : INCR key
     * 给 value 加 1,value 必须是整数
     *
     * @param key
     * @return
     */
    public Long inCr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 实现命令 : INCRBY key 整数
     * 给 value 加 上一个整数,value 必须是整数
     *
     * @param key
     * @return
     */
    public Long inCrBy(String key, Long number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    /**
     * 实现命令 : INCRBYFLOAT key 数
     * 给 value 加上一个小数,value 必须是数
     *
     * @param key
     * @return
     */
    public Double inCrByFloat(String key, double number) {
        return redisTemplate.opsForValue().increment(key, number);
    }


    /**
     * 实现命令 : DECR key
     * 给 value 减去 1,value 必须是整数
     *
     * @param key
     * @return
     */
    public Long deCr(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }


    /**
     * 实现命令 : DECRBY key 整数
     * 给 value 减去一个整数,value 必须是整数
     *
     * @param key
     * @return
     */
    public Long deCcrBy(String key, Long number) {
        return redisTemplate.opsForValue().decrement(key, number);
    }


    /************************************************* String 相关操作 *************************************************/


    /************************************************* Hash 相关操作 ***************************************************/

    /**
     * 实现命令 : HSET key field value
     * 添加 hash 类型的键值对，如果字段已经存在，则将其覆盖。
     *
     * @param key
     * @param field
     * @param value
     */
    public void hSet(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }


    /**
     * 实现命令 : HSET key field1 value1 [field2 value2 ...]
     * 添加 hash 类型的键值对，如果字段已经存在，则将其覆盖。
     *
     * @param key
     * @param map
     */
    public void hSet(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }


    /**
     * 实现命令 : HSETNX key field value
     * 添加 hash 类型的键值对，如果字段不存在，才添加
     *
     * @param key
     * @param field
     * @param value
     */
    public boolean hSetNx(String key, String field, Object value) {
        Boolean result = redisTemplate.opsForHash().putIfAbsent(key, field, value);
        if (result == null) {
            return false;
        }
        return result;
    }


    /**
     * 实现命令 : HGET key field
     * 返回 field 对应的值
     *
     * @param key
     * @param field
     * @return
     */
    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }


    /**
     * 实现命令 : HMGET key field1 [field2 ...]
     * 返回 多个 field 对应的值
     *
     * @param key
     * @param fields
     * @return
     */
    public List<Object> hGet(String key, String... fields) {
        Set<Object> fieldSet = Stream.of(fields).collect(Collectors.toSet());
        return redisTemplate.opsForHash().multiGet(key, fieldSet);
    }


    /**
     * 实现命令 : HGETALL key
     * 返回所以的键值对
     *
     * @param key
     * @return
     */
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }


    /**
     * 实现命令 : HKEYS key
     * 获取所有的 field
     *
     * @param key
     * @return
     */
    public Set<Object> hKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }


    /**
     * 实现命令 : HVALS key
     * 获取所有的 value
     *
     * @param key
     * @return
     */
    public List<Object> hValue(String key) {
        return redisTemplate.opsForHash().values(key);
    }


    /**
     * 实现命令 : HDEL key field [field ...]
     * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     *
     * @param key
     * @param fields
     */
    public Long hDel(String key, Object... fields) {
        return redisTemplate.opsForHash().delete(key, fields);
    }


    /**
     * 实现命令 : HEXISTS key field
     * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     *
     * @param key
     * @param field
     */
    public boolean hExists(String key, String field) {
        Boolean result = redisTemplate.opsForHash().hasKey(key, field);
        if (result == null) {
            return false;
        }
        return result;
    }


    /**
     * 实现命令 : HLEN key
     * 获取 hash 中字段:值对的数量
     *
     * @param key
     */
    public Long hLen(String key) {
        return redisTemplate.opsForHash().size(key);
    }


    /**
     * 实现命令 : HSTRLEN key field
     * 获取字段对应值的长度
     *
     * @param key
     * @param field
     */
    public Long hStrLen(String key, String field) {
        return redisTemplate.opsForHash().lengthOfValue(key, field);
    }


    /**
     * 实现命令 : HINCRBY key field 整数
     * 给字段的值加上一个整数
     *
     * @param key
     * @param field
     * @return 运算后的值
     */
    public Long hInCrBy(String key, String field, long number) {
        return redisTemplate.opsForHash().increment(key, field, number);
    }


    /**
     * 实现命令 : HINCRBYFLOAT key field 浮点数
     * 给字段的值加上一个浮点数
     *
     * @param key
     * @param field
     * @return 运算后的值
     */
    public Double hInCrByFloat(String key, String field, Double number) {
        return redisTemplate.opsForHash().increment(key, field, number);
    }


    /************************************************* Hash 相关操作 ***************************************************/


    /************************************************* List 相关操作 ***************************************************/

    /**
     * 实现命令 : LPUSH key 元素1 [元素2 ...]
     * 在最左端推入元素
     *
     * @param key
     * @param values
     * @return 执行 LPUSH 命令后，列表的长度。
     */
    public Long lPush(String key, Object... values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }


    /**
     * 实现命令 : RPUSH key 元素1 [元素2 ...]
     * 在最右端推入元素
     *
     * @param key
     * @param values
     * @return 执行 RPUSH 命令后，列表的长度。
     */
    public Long rPush(String key, Object... values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }


    /**
     * 实现命令 : LPOP key
     * 弹出最左端的元素
     *
     * @param key
     * @return 弹出的元素
     */
    public Object lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 实现命令 : RPOP key
     * 弹出最右端的元素
     *
     * @param key
     * @return 弹出的元素
     */
    public Object rPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }


    /**
     * 实现命令 : BLPOP key
     * (阻塞式)弹出最左端的元素，如果 key 中没有元素，将一直等待直到有元素或超时为止
     *
     * @param key
     * @param timeout 等待的时间，单位秒
     * @return 弹出的元素
     */
    public Object bLPop(String key, int timeout) {
        return redisTemplate.opsForList().leftPop(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 实现命令 : BRPOP key
     * (阻塞式)弹出最右端的元素，将一直等待直到有元素或超时为止
     *
     * @param key
     * @return 弹出的元素
     */
    public Object bRPop(String key, int timeout) {
        return redisTemplate.opsForList().rightPop(key, timeout, TimeUnit.SECONDS);
    }


    /**
     * 实现命令 : LINDEX key index
     * 返回指定下标处的元素，下标从0开始
     *
     * @param key
     * @param index
     * @return 指定下标处的元素
     */
    public Object lIndex(String key, int index) {
        return redisTemplate.opsForList().index(key, index);
    }


    /**
     * 实现命令 : LINSERT key BEFORE|AFTER 目标元素 value
     * 在目标元素前或后插入元素
     *
     * @param key
     * @param position
     * @param pivot
     * @param value
     * @return
     */
    public Long lInsert(String key, Position position, Object pivot, Object value) {
        switch (position) {
            case AFTER:
                return redisTemplate.opsForList().rightPush(key, pivot, value);
            case BEFORE:
                return redisTemplate.opsForList().rightPush(key, pivot, value);
            default:
                return null;
        }
    }


    /**
     * 实现命令 : LRANGE key 开始下标 结束下标
     * 获取指定范围的元素,下标从0开始，包括开始下标，也包括结束下标(待验证)
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<Object> lRange(String key, int start, int end) {
        return redisTemplate.opsForList().range(key, start, end);
    }


    /**
     * 实现命令 : LLEN key
     * 获取 list 的长度
     *
     * @param key
     * @return
     */
    public Long lLen(String key) {
        return redisTemplate.opsForList().size(key);
    }


    /**
     * 实现命令 : LREM key count 元素
     * 删除 count 个指定元素,
     *
     * @param key
     * @param count count > 0: 从头往尾移除指定元素。count < 0: 从尾往头移除指定元素。count = 0: 移除列表所有的指定元素。(未验证)
     * @param value
     * @return
     */
    public Long lLen(String key, int count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }


    /**
     * 实现命令 : LSET key index 新值
     * 更新指定下标的值,下标从 0 开始，支持负下标，-1表示最右端的元素(已验证)
     *
     * @param key
     * @param index
     * @param value
     * @return
     */
    public void lSet(String key, int index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }


    /**
     * 实现命令 : LTRIM key 开始下标 结束下标
     * 裁剪 list。[01234] 的 `LTRIM key 1 -2` 的结果为 [123]
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public void lTrim(String key, int start, int end) {
        redisTemplate.opsForList().trim(key, start, end);
    }


    /**
     * 实现命令 : RPOPLPUSH 源list 目标list
     * 将 源list 的最右端元素弹出，推入到 目标list 的最左端，
     *
     * @param sourceKey 源list
     * @param targetKey 目标list
     * @return 弹出的元素
     */
    public Object rPopLPush(String sourceKey, String targetKey) {
        return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, targetKey);
    }


    /**
     * 实现命令 : BRPOPLPUSH 源list 目标list timeout
     * (阻塞式)将 源list 的最右端元素弹出，推入到 目标list 的最左端，如果 源list 没有元素，将一直等待直到有元素或超时为止
     *
     * @param sourceKey 源list
     * @param targetKey 目标list
     * @param timeout   超时时间，单位秒， 0表示无限阻塞
     * @return 弹出的元素
     */
    public Object bRPopLPush(String sourceKey, String targetKey, int timeout) {
        return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, targetKey, timeout, TimeUnit.SECONDS);
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
    public Long sAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }


    /**
     * 实现命令 : SREM key member1 [member2 ...]
     * 删除指定的成员
     *
     * @param key
     * @param values
     * @return 删除成功的个数
     */
    public Long sRem(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }


    /**
     * 实现命令 : SCARD key
     * 获取set中的成员数量
     *
     * @param key
     * @return
     */
    public Long sCard(String key) {
        return redisTemplate.opsForSet().size(key);
    }


    /**
     * 实现命令 : SISMEMBER key member
     * 查看成员是否存在
     *
     * @param key
     * @param values
     * @return
     */
    public boolean sIsMember(String key, Object... values) {
        Boolean result = redisTemplate.opsForSet().isMember(key, values);
        if (null == result) {
            return false;
        }
        return result;
    }


    /**
     * 实现命令 : SMEMBERS key
     * 获取所有的成员
     *
     * @param key
     * @return
     */
    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }


    /**
     * 实现命令 : SMOVE 源key 目标key member
     * 移动成员到另一个集合
     *
     * @param sourceKey 源key
     * @param targetKey 目标key
     * @param value
     * @return
     */
    public boolean sMove(String sourceKey, String targetKey, Object value) {
        Boolean result = redisTemplate.opsForSet().move(sourceKey, value, targetKey);
        if (null == result) {
            return false;
        }
        return result;
    }


    /**
     * 实现命令 : SDIFF key [otherKey ...]
     * 求 key 的差集
     *
     * @param key
     * @param otherKeys
     * @return
     */
    public Set<Object> sDiff(String key, String... otherKeys) {
        List<String> otherKeyList = Stream.of(otherKeys).collect(Collectors.toList());
        return redisTemplate.opsForSet().difference(key, otherKeyList);
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
    public Long sDiffStore(String targetKey, String key, String... otherKeys) {
        List<String> otherKeyList = Stream.of(otherKeys).collect(Collectors.toList());
        return redisTemplate.opsForSet().differenceAndStore(key, otherKeyList, targetKey);
    }


    /**
     * 实现命令 : SINTER key [otherKey ...]
     * 求 key 的交集
     *
     * @param key
     * @param otherKeys
     * @return
     */
    public Set<Object> sInter(String key, String... otherKeys) {
        List<String> otherKeyList = Stream.of(otherKeys).collect(Collectors.toList());
        return redisTemplate.opsForSet().intersect(key, otherKeyList);
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
    public Long sInterStore(String targetKey, String key, String... otherKeys) {
        List<String> otherKeyList = Stream.of(otherKeys).collect(Collectors.toList());
        return redisTemplate.opsForSet().intersectAndStore(key, otherKeyList, targetKey);
    }


    /**
     * 实现命令 : SUNION key [otherKey ...]
     * 求 key 的并集
     *
     * @param key
     * @param otherKeys
     * @return
     */
    public Set<Object> sUnion(String key, String... otherKeys) {
        List<String> otherKeyList = Stream.of(otherKeys).collect(Collectors.toList());
        return redisTemplate.opsForSet().union(key, otherKeyList);
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
    public Long sUnionStore(String targetKey, String key, String... otherKeys) {
        List<String> otherKeyList = Stream.of(otherKeys).collect(Collectors.toList());
        return redisTemplate.opsForSet().unionAndStore(key, otherKeyList, targetKey);
    }


    /**
     * 实现命令 : SPOP key [count]
     * 随机删除(弹出)指定个数的成员
     *
     * @param key
     * @return
     */
    public Object sPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 实现命令 : SPOP key [count]
     * 随机删除(弹出)指定个数的成员
     *
     * @param key
     * @param count 个数
     * @return
     */
    public List<Object> sPop(String key, int count) {
        return redisTemplate.opsForSet().pop(key, count);
    }


    /**
     * 实现命令 : SRANDMEMBER key [count]
     * 随机返回指定个数的成员
     *
     * @param key
     * @return
     */
    public Object sRandMember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }


    /**
     * 实现命令 : SRANDMEMBER key [count]
     * 随机返回指定个数的成员
     * 如果 count 为正数，随机返回 count 个不同成员
     * 如果 count 为负数，随机选择 1 个成员，返回 count 个
     *
     * @param key
     * @param count 个数
     * @return
     */
    public List<Object> sRandMember(String key, int count) {
        return redisTemplate.opsForSet().randomMembers(key, count);
    }


    /************************************************** SET 相关操作 ***************************************************/


    /*********************************************** Sorted SET 相关操作 ***********************************************/

    /**
     * 实现命令 : ZADD key score member
     * 添加一个 成员/分数 对
     *
     * @param key
     * @param value 成员
     * @param score 分数
     * @return
     */
    public boolean zAdd(String key, double score, Object value) {
        Boolean result = redisTemplate.opsForZSet().add(key, value, score);
        if (result == null) {
            return false;
        }
        return result;
    }


    /**
     * 实现命令 : ZREM key member [member ...]
     * 删除成员
     *
     * @param key
     * @param values
     * @return
     */
    public Long zRem(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }


    /**
     * 实现命令 : ZREMRANGEBYRANK key start stop
     * 删除 start下标 和 end下标间的所有成员
     * 下标从0开始，支持负下标，-1表示最右端成员，包括开始下标也包括结束下标 (已验证)
     *
     * @param key
     * @param start 开始下标
     * @param end   结束下标
     * @return
     */
    public Long zRemRangeByRank(String key, int start, int end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }


    /**
     * 实现命令 : ZREMRANGEBYSCORE key start stop
     * 删除分数段内的所有成员
     * 包括min也包括max (已验证)
     *
     * @param key
     * @param min 小分数
     * @param max 大分数
     * @return
     */
    public Long zRemRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }


    /**
     * 实现命令 : ZSCORE key member
     * 获取成员的分数
     *
     * @param key
     * @param value
     * @return
     */
    public Double zScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }


    /**
     * 实现命令 : ZINCRBY key 带符号的双精度浮点数 member
     * 增减成员的分数
     *
     * @param key
     * @param value
     * @param delta 带符号的双精度浮点数
     * @return
     */
    public Double zInCrBy(String key, Object value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }


    /**
     * 实现命令 : ZCARD key
     * 获取集合中成员的个数
     *
     * @param key
     * @return
     */
    public Long zCard(String key) {
        return redisTemplate.opsForZSet().size(key);
    }


    /**
     * 实现命令 : ZCOUNT key min max
     * 获取某个分数范围内的成员个数，包括min也包括max (未验证)
     *
     * @param key
     * @param min 小分数
     * @param max 大分数
     * @return
     */
    public Long zCount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }


    /**
     * 实现命令 : ZRANK key member
     * 按分数从小到大获取成员在有序集合中的排名
     *
     * @param key
     * @param value
     * @return
     */
    public Long zRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }


    /**
     * 实现命令 : ZREVRANK key member
     * 按分数从大到小获取成员在有序集合中的排名
     *
     * @param key
     * @param value
     * @return
     */
    public Long zRevRank(String key, Object value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }


    /**
     * 实现命令 : ZRANGE key start end
     * 获取 start下标到 end下标之间到成员，并按分数从小到大返回
     * 下标从0开始，支持负下标，-1表示最后一个成员，包括开始下标，也包括结束下标(未验证)
     *
     * @param key
     * @param start 开始下标
     * @param end   结束下标
     * @return
     */
    public Set<Object> zRange(String key, int start, int end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }


    /**
     * 实现命令 : ZREVRANGE key start end
     * 获取 start下标到 end下标之间到成员，并按分数从小到大返回
     * 下标从0开始，支持负下标，-1表示最后一个成员，包括开始下标，也包括结束下标(未验证)
     *
     * @param key
     * @param start 开始下标
     * @param end   结束下标
     * @return
     */
    public Set<Object> zRevRange(String key, int start, int end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }


    /**
     * 实现命令 : ZRANGEBYSCORE key min max
     * 获取分数范围内的成员并按从小到大返回
     * (未验证)
     *
     * @param key
     * @param min 小分数
     * @param max 大分数
     * @return
     */
    public Set<Object> zRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }


    /**
     * 实现命令 : ZRANGEBYSCORE key min max LIMIT offset count
     * 分页获取分数范围内的成员并按从小到大返回
     * 包括min也包括max(未验证)
     *
     * @param key
     * @param min    小分数
     * @param max    大分数
     * @param offset 开始下标，下标从0开始
     * @param count  取多少条
     * @return
     */
    public Set<Object> zRangeByScore(String key, double min, double max, int offset, int count) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
    }


    /**
     * 实现命令 : ZREVRANGEBYSCORE key min max
     * 获取分数范围内的成员并按从大到小返回
     * (未验证)
     *
     * @param key
     * @param min 小分数
     * @param max 大分数
     * @return
     */
    public Set<Object> zRevRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }


    /**
     * 实现命令 : ZREVRANGEBYSCORE key min max LIMIT offset count
     * 分页获取分数范围内的成员并按从大到小返回
     * 包括min也包括max(未验证)
     *
     * @param key
     * @param min    小分数
     * @param max    大分数
     * @param offset 开始下标，下标从0开始
     * @param count  取多少条
     * @return
     */
    public Set<Object> zRevRangeByScore(String key, double min, double max, int offset, int count) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
    }

    /*********************************************** Sorted SET 相关操作 ***********************************************/

}