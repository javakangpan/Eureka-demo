package demo.Controller;

import cn.hutool.core.date.DateUtil;
import com.google.gson.Gson;
import demo.model.Coffee;
import demo.model.CoffeeCache;
import demo.model.CoffeeRequest;
import demo.repository.CoffeeCacheRepository;
import demo.repository.CoffeeRepository;
import demo.util.RedisLock;
import demo.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@Slf4j
public class CoffeeController {

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Autowired
    private CoffeeCacheRepository cacheRepository;
    @Autowired
    private RedisLock redisLock;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping(path = "/", params = "!name")
    public List<Coffee> getAll() {
        return coffeeRepository.findAll(Sort.by("id"));
    }

    @GetMapping(path = "/", params = "name")
    public Coffee getByName(@RequestParam String name) {
        Optional<CoffeeCache> cached = cacheRepository.findOneByName(name);
        if (cached.isPresent()) {
            CoffeeCache coffeeCache = cached.get();
            Coffee coffee = Coffee.builder()
                    .name(coffeeCache.getName())
                    .price(coffeeCache.getPrice())
                    .build();
            log.info("Coffee {} found in cache.", coffeeCache);
            return Optional.of(coffee).get();
        } else {
            HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
            if (redisTemplate.hasKey("coffee") && hashOperations.hasKey("coffee", name)) {
                log.info("Get coffee {} from Redis.", name);
                Coffee coffee = new Gson().fromJson(hashOperations.get("coffee", name),Coffee.class);
                return coffee;
            }else {
                Coffee coffee = coffeeRepository.findByName(name);
                if(coffee != null){
                    Gson gson = new Gson();
                    String str = gson.toJson(coffee);
                    log.info("coffee:{}",str);
                    redisUtil.hset("coffee",name,str,60*5);
                    CoffeeCache coffeeCache = CoffeeCache.builder()
                            .id(coffee.getId())
                            .name(coffee.getName())
                            .price(coffee.getPrice())
                            .build();
                    log.info("Save Coffee {} to cache.", coffeeCache);
                    cacheRepository.save(coffeeCache);
                    return coffee;
                }
            }
        }
        return null;
    }


    @PostMapping(path ="/")
    public boolean updateCoffeeCount(@RequestBody CoffeeRequest coffeeRequest) {
        //当前时间+超时时间
        long time = System.currentTimeMillis() + 5*1000;
        Date date = DateUtil.date(time);
        log.info("=========================> time:{}",DateUtil.formatDateTime(date));
        try {
            if(!redisLock.lock(coffeeRequest.getName(),String.valueOf(time))){
                return false;
            }
            Coffee coffee = coffeeRepository.findByName(coffeeRequest.getName());
            log.info("coffee:{}",coffee);
            int num = coffee.getCount();
            log.info("count:{}",num);
            if(num == 0) {
                return false;
            }
            if(coffeeRequest.isFlag() == true && coffeeRequest.getCount() <= num){
                coffeeRepository.updateCoffeeCount(coffeeRequest.getName(),num - coffeeRequest.getCount());
            }
        } catch (Exception e) {
            log.error("error:{}",e.getMessage());
        } finally {
            redisLock.unlock(coffeeRequest.getName(),String.valueOf(time));
        }
        return false;
    }
}
