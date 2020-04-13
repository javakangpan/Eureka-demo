package demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 模拟调用API
 */
@RestController
@Slf4j
public class CustomerController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/")
    public String getAPIData() {
        log.info("================> DiscoveryClient: {}", discoveryClient.getClass().getName());
        discoveryClient.getInstances("waiter-service").forEach(s -> {
            log.info("================> Host: {}, Port: {}", s.getHost(), s.getPort());
        });

        ParameterizedTypeReference<Object> ptr = new ParameterizedTypeReference<Object>() {
        };
        ResponseEntity<Object> list = restTemplate
                .exchange("http://waiter-service/", HttpMethod.GET, null, ptr);
        log.info("Coffee: {}", list);
        return list.toString();
    }

}
