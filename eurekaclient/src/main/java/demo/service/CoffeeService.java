package demo.service;

import demo.model.Coffee;
import demo.repository.CoffeeRepository;
import demo.test.logging.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CoffeeService {

    @Autowired
    private CoffeeRepository coffeeRepository;

    //常用的注解有：@EnableCaching、@Cacheable、@CacheEvict、@CachePut、@Caching、@CacheConfig。
    //EnableCaching 开启缓存
    // Cacheable 方法执行前 根据key查询 命中返回 没有命中执行方法 用于类或方法上
    //CachePut 用于类或方法上 方法执行后 键值对存入缓存
    //key的来源可分为三类，分别是：默认的、keyGenerator生成的、主动指定的
    //方法无参，不指定key，没有keyGenerator 默认SimpleKey[]
    //一个参数，不指定key，没有keyGenerator 默认为key.toString(对象一样)
    //多个参数，不指定key，没有keyGenerator 默认为SimpleKey[参数1.toString,参数2.toString...]
    //指定key 会覆盖keyGenerator生成的key(详情见LocalCacheConfigToKey 类)

    //Spring Expression Language (SpEL)
    //key = "#str" 等价于 key = "#p0" 等价于 key = "#a0" (理解：params,args)
    //.获取属性或方法 #p0.name  #a1.hashCode()
    //key = "#root" 根对象  #root.args[0] 等价于 #p0
    //#root.target #root.targetClass
    // #root.method(demo.controller.CoffeeController.getAll())
    // #root.methodName(该方法名 getAll)

    //cacheNames 多个命名空间按顺序查找
    //condition 运行目标方法前 条件满足 激活注解 命中缓存
    //unless    是否令注解在运行目标方法后失效 默认false
    //allEntries 是否清除指定命名空间中的所有数据，默认为false 属性主要出现在@CacheEvict注解中
    //beforeInvocation 属性主要出现在@CacheEvict注解中，表示是否在目标方法执行前此注解生效, 默认为false，即:目标方法执行完毕后此注解生效。

    //void方法无返回值 也会进行缓存 value为null
    @Cacheable(cacheNames = "COFFEE3")
    @Log(value = "查询所有的咖啡")
    public List<Coffee> findAllCoffee() {
        return coffeeRepository.findAll(Sort.by("id"));
    }

    @Cacheable(cacheNames = "COFFEE4")
    @Log(value = "查询所有的咖啡")
    public List<Coffee> findAllCoffee2() {
        return coffeeRepository.findAll(Sort.by("id"));
    }

    //测试 查询缓存绕过了登录校验
    // @Auth 写在service没有效果 只能拦截controller层方法
    //@Auth(required = true)
    @Log(value = "查询所有的咖啡")
    public List<Coffee> findAllCoffee3() {
        return coffeeRepository.findAll(Sort.by("id"));
    }
}
