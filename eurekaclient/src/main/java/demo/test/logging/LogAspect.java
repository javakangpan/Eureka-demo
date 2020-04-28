package demo.test.logging;

import demo.repository.LogRepository;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * joinPoint.getTarget().getClass().getSimpleName()获取的是被切的类名
 * joinPoint.getSignature().getName()获取的是被切的方法名
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    ThreadLocal<Long> currentTime = new ThreadLocal<>();
    @Resource
    private LogRepository logRepository;
    /**
     * 配置切入点
     */
    @Pointcut("@annotation(demo.test.logging.Log)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        SubMethod subMethod = new SubMethod(joinPoint).invoke();
        HttpServletRequest request = subMethod.getRequest();
        Log aopLog = subMethod.getAopLog();
        String methodName = subMethod.getMethodName();
        StringBuilder params = subMethod.getParams();
        Logs logs = Logs.builder()
                .returnValue(joinPoint.proceed().toString())
                .browser(getBrowser(request))
                .description(aopLog.value())
                .requestIp(getIp(request))
                .params(params.toString() + "}")
                .method(methodName)
                .time(System.currentTimeMillis() - currentTime.get())
                .logType("info")
                .address(request.getRequestURI()).build();
        logRepository.save(logs);
        log.info("日志查询:{}",logRepository.findAll());
        currentTime.remove();
        return joinPoint.proceed();
    }

    /**
     * 配置异常通知
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        SubMethod subMethod = new SubMethod((ProceedingJoinPoint)joinPoint).invoke();
        HttpServletRequest request = subMethod.getRequest();
        Log aopLog = subMethod.getAopLog();
        String methodName = subMethod.getMethodName();
        StringBuilder params = subMethod.getParams();
        Logs logs = Logs.builder()
                .browser(getBrowser(request))
                .description(aopLog.value())
                .requestIp(getIp(request))
                .params(params.toString() + "}")
                .method(methodName)
                .time(System.currentTimeMillis() - currentTime.get())
                .logType("error")
                .exceptionDetail(e.getMessage())
                .address(request.getRequestURI()).build();
        logRepository.save(logs);
        log.info("日志查询:{}",logRepository.findAll());
        currentTime.remove();
    }
    /**
     * 获取浏览器信息
     */
    public  String getBrowser(HttpServletRequest request){
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        return browser.getName();
    }

    /**
     * 获取ip地址
     */
    public  String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if  (localhost.equals(ip))  {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    private class SubMethod {
        private ProceedingJoinPoint joinPoint;
        private HttpServletRequest request;
        private Log aopLog;
        private String methodName;
        private StringBuilder params;

        public SubMethod(ProceedingJoinPoint joinPoint) {
            this.joinPoint = joinPoint;
        }

        public HttpServletRequest getRequest() {
            return request;
        }

        public Log getAopLog() {
            return aopLog;
        }

        public String getMethodName() {
            return methodName;
        }

        public StringBuilder getParams() {
            return params;
        }

        public SubMethod invoke() {
            currentTime.set(System.currentTimeMillis());
            request = RequestHolder.getHttpServletRequest();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            aopLog = method.getAnnotation(Log.class);
            //方法路径
            methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";
            params = new StringBuilder("{");
            //参数值
            Object[] args = joinPoint.getArgs();
            //参数名称
            String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
            if(args != null){
                for (int i = 0; i < args.length; i++) {
                    params.append(" ").append(argNames[i]).append(": ").append(args[i]);
                }
            }
            return this;
        }
    }
}
