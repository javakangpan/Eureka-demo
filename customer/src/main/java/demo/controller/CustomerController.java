package demo.controller;

import demo.model.Coffee;
import demo.model.CoffeeRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

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

        ParameterizedTypeReference<List<Coffee>> ptr = new ParameterizedTypeReference<List<Coffee>>() {
        };
        ResponseEntity<List<Coffee>> list = restTemplate
                .exchange("http://waiter-service/", HttpMethod.GET, null, ptr);
        list.getBody().forEach(c -> log.info("Coffee: {}", c));

        CoffeeRequest coffeeRequest = CoffeeRequest.builder().name("espresso").count(1).flag(true).build();
        RequestEntity<CoffeeRequest> requestEntity = RequestEntity
                .post(UriComponentsBuilder.fromUriString("http://waiter-service/")
                .build().toUri()).contentType(MediaType.APPLICATION_JSON).body(coffeeRequest);
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(requestEntity,Boolean.class);
        log.info("responseEntity:{}",requestEntity);


        return list.getBody().toString();
    }

}
