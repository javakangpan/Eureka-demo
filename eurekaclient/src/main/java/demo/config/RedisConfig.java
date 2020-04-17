package demo.config;

import demo.support.BytesToMoneyConverter;
import demo.support.MoneyToBytesConverter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;

/**
 * RedisProperties 配置
 * Jedis或者Lettuce客户端
 * LettuceConnectionFactory 与 JedisConnectionFactory
 * 单节点 RedisStandaloneConfiguration
 * 哨兵 RedisSentinelConfiguration
 * 集群 RedisClusterConfiguration
 *  配置集群节点信息，访问其中一个就能取得所有节点信息
 *  spring.redis.cluster.nodes=127.0.0.1:23679,127.0.0.1:23680,127.0.0.1:23681
 *  设置最大的重定向次数的，RedisCluster里，你把请求发到了某个节点上，它发现这个KEY不在自己这里，就会告诉你重定向到另一个节点上去找。
 * 	spring.redis.cluster.max-redirects=3
 *  Lettuce 内置支持读写分离
 *  只读主 只读从
 *  优先读主 优先读从
 *      LettuceClientConfiguration
 *      LettucePoolingClientConfiguration
 *      lettuceClientConfigurationBuilderCustomizer
 * opsForValue：对应 String（字符串）
 * opsForZSet：对应 ZSet（有序集合）
 * opsForHash：对应 Hash（哈希）
 * opsForList：对应 List（列表）
 * opsForSet：对应 Set（集合）
 * opsForGeo：** 对应 GEO（地理位置）
 */

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {
    /**
     * 默认情况下的模板只能支持RedisTemplate<String, String>，也就是只能存入字符串，因此支持序列化
     * 1、使用Jackson2JsonRedisSerializer需要指明序列化的类Class，可以使用Obejct.class
     * 2、使用GenericJacksonRedisSerializer比Jackson2JsonRedisSerializer效率低，占用内存高。
     * 3、GenericJacksonRedisSerializer反序列化带泛型的数组类会报转换异常，解决办法存储以JSON字符串存储。
     * 4、GenericJacksonRedisSerializer和Jackson2JsonRedisSerializer都是以JSON格式去存储数据，都可以作为Redis的序列化方式。
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //设置String key 和value序列化模式
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;

    }
    /**
     * 对hash类型的数据操作
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 对链表类型的数据操作
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    public RedisCustomConversions redisCustomConversions() {
        return new RedisCustomConversions(
                Arrays.asList(new MoneyToBytesConverter(), new BytesToMoneyConverter()));
    }
}
