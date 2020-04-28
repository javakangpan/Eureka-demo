package demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import demo.mapper.UserMapper;
import demo.model.TokenCache;
import demo.model.User;
import demo.repository.TokenCacheRepository;
import demo.test.logging.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class TestSecurityController {
    @Resource
    private UserMapper userMapper;
    @Resource
    private TokenCacheRepository tokenCacheRepository;

    @PostMapping("/login")
    @Log
    @ResponseBody
    public Object login(@RequestBody User user) {
        log.info("login");
        JSONObject jsonObject = new JSONObject();
        User u = userMapper.loadUserByUsername(user.getUsername());
        if(u == null) {
            jsonObject.put("message","账号错误");
            return jsonObject;
        } else {
            if(!user.getPassword().equals(u.getPassword())) {
                jsonObject.put("message","密码错误");
                return jsonObject;
            } else {
                String token =  JWT.create().withAudience(u.getId().toString())// 将 user id 保存到 token 里面
                        .sign(Algorithm.HMAC256(u.getPassword()));// 以 password 作为 token 的密钥
                TokenCache tokenCache = TokenCache.builder().id(u.getId()).token(token).name("token").build();
                tokenCacheRepository.save(tokenCache);
                jsonObject.put("message","登录成功");
                return jsonObject;
            }
        }
    }
}
