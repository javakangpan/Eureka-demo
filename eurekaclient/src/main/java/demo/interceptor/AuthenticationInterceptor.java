package demo.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import demo.annotations.Auth;
import demo.mapper.UserMapper;
import demo.model.TokenCache;
import demo.model.User;
import demo.repository.LogRepository;
import demo.repository.TokenCacheRepository;
import demo.test.logging.Logs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * * ==>SpringMVC 的拦截器
 *  * 核心接口 HandlerInterceptor
 *  JWT 例子
 * JWT头
 * 有效载荷
 *  iss：发行人
 * exp：到期时间
 * sub：主题
 * aud：用户
 * nbf：在此之前不可用
 * iat：发布时间
 * jti：JWT ID用于标识该JWT
 * 签名哈希
 */
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
    private ThreadLocal<StopWatch> threadLocal = new ThreadLocal<StopWatch>();
    @Resource
    private UserMapper userMapper;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private TokenCacheRepository tokenCacheRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("=====================>preHandle");
        StopWatch stopWatch = new StopWatch();
        threadLocal.set(stopWatch);
        stopWatch.start();
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        ////检查注释
        if(method.isAnnotationPresent(Auth.class)) {
            Auth auth = method.getAnnotation(Auth.class);
            log.info("=====================>auth:{}",auth.required());
            if(auth.required()) {
                // 从 http 请求头中取出 token
                //String token = request.getHeader("token");
                String token = "";
                Optional<TokenCache> tokenCache = tokenCacheRepository.findOneByName("token");
                if(tokenCache.isPresent()) {
                   token = tokenCache.get().getToken();
                   log.info("tokenCache:{}",tokenCache.get());
                }
                if(StringUtils.isEmpty(token)) {
                    throw new RuntimeException("无token，请重新登录");
                }else {
                    String id = JWT.decode(token).getAudience().get(0);
                    Long userId = Long.valueOf(id);
                    User user = userMapper.loadUserByUserId(userId);
                    if(user == null) {
                        throw new RuntimeException("用户不存在，请重新登录");
                    }
                    JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
                    return true;
                }

            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        threadLocal.get().stop();
        threadLocal.get().start();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        StopWatch stopWatch = threadLocal.get();
        stopWatch.stop();
        String method = handler.getClass().getSimpleName();
        if(handler instanceof HandlerMethod) {
            String beanType = ((HandlerMethod)handler).getBeanType().getName();
            String methodName = ((HandlerMethod) handler).getMethod().getName();
            method = beanType + "." + methodName;
        }
        String exception = ex == null ? "" : ex.getMessage();
        Logs logs = Logs.builder()
                .time(stopWatch.getTotalTimeMillis())
                .method(method)
                .exceptionDetail(exception)
                .address(request.getRequestURI())
                .description("header:" + response.getHeader("token") +
                        "contentType:" + response.getContentType() + ";status:" + response.getStatus())
                .logType("拦截器拦截").build();
        threadLocal.remove();
        logRepository.save(logs);
    }
}
