package demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
/*import org.springframework.cloud.client.discovery.EnableDiscoveryClient;*/
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
/**
 * 在SpringBootApplication上使用@ServletComponentScan注解后，
 * Servlet、Filter、Listener可以直接
 * 通过@WebServlet、@WebFilter、@WebListener注解自动注册。
 * @ComponentScan SpringBoot在写启动类的时候如果不使用@ComponentScan指明对象扫描范围，
 * 默认指扫描当前启动类所在的包里的对象
 */
@SpringBootApplication
/*@EnableDiscoveryClient*/
@EnableJpaRepositories
@EnableRedisRepositories
@MapperScan("demo.mapper")
@EnableAsync
@ServletComponentScan
public class WaiterServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(WaiterServiceApplication.class, args);
    }
}
