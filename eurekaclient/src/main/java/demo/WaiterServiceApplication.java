package demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*import org.springframework.cloud.client.discovery.EnableDiscoveryClient;*/
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
/*@EnableDiscoveryClient*/
@EnableJpaRepositories
@EnableRedisRepositories
@MapperScan("demo.mapper")
@EnableAsync
public class WaiterServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(WaiterServiceApplication.class, args);
    }
}
