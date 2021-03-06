package demo.controller;

import cn.hutool.core.date.DateUtil;
import com.google.gson.Gson;
import demo.annotations.Auth;
import demo.mapStruct.MapStructStuCoffeeMapper;
import demo.mapper.CoffeeMapper;
import demo.mapper.StudentMapper;
import demo.model.*;
import demo.repository.CoffeeCacheRepository;
import demo.repository.CoffeeRepository;
import demo.service.CoffeeService;
import demo.util.RedisLock;
import demo.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;



@Controller
@Slf4j
@RequestMapping("/coffee")
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
    @Resource
    private CoffeeMapper coffeeMapper;
    @Resource
    private StudentMapper studentMapper;
    @Autowired
   private MapStructStuCoffeeMapper mapStructStuCoffeeMapper;

    //Constructor >> @Autowired >> @PostConstruct
    @PostConstruct
    public void init() {
        log.info("服务器加载Servlet->构造函数->@PostConstruct-Init->Service->destroy->@PreDestroy->服务器卸载Servlet");
    }

    @GetMapping("/testMapStruct")
    @ResponseBody
    public MapStructStuCoffee testMapStruct() {
        Coffee coffee = getById(1);
        Student student = studentMapper.getStudentById(1);
        MapStructStuCoffee mapStructStuCoffee = mapStructStuCoffeeMapper.from(student,coffee);
        log.info("testMapStruct ==> {}",mapStructStuCoffee);
        return mapStructStuCoffee;
    }

    @GetMapping(path = "/{id}")
    @ResponseBody
    public Coffee getById(@PathVariable(name = "id") long id) {
        log.info("id:{}",id);
        //测试
        log.info("student:{}",studentMapper.getStudentByIdWithClassInfo(1));
        return coffeeMapper.findById(id);
    }

    @Autowired
    private CoffeeService coffeeService;

    //测试不同缓存时间 缓存30秒
    @GetMapping(path = "/test1", params = "!name")
    @ResponseBody
    public List<Coffee> getAll() {
        return coffeeService.findAllCoffee();
    }
    //测试不同缓存时间 缓存1分钟 登录校验
    @GetMapping(path = "/test2", params = "!name")
    @ResponseBody
    @Auth(required = true)
    public List<Coffee> getAll2() {
        return coffeeService.findAllCoffee2();
    }

    //测试登录校验
    @GetMapping(path = "/test3", params = "!name")
    @ResponseBody
    @Auth(required = true)
    public List<Coffee> getAll3() {
        return coffeeService.findAllCoffee3();
    }

    @GetMapping(path = "/", params = "name")
    @ResponseBody
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
    @ResponseBody
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
