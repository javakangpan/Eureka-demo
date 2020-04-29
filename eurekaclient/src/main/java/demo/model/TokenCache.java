package demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
//ttl 单位秒
@RedisHash(value = "token",timeToLive = 60*60)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenCache {
    //@Id 注解标注缓存条目主键，也可以通过 @Indexed 注解来增加其他缓存条目
    @Id
    private Long id;
    @Indexed //根据sessionId 查询 自定义方法
    private String sessionId;
    private String token;
}
