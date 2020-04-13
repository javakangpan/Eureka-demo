package demo.Controller;

import demo.model.Coffee;
import demo.repository.CoffeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CoffeeController {

    @Autowired
    private CoffeeRepository coffeeRepository;

    @GetMapping(path = "/", params = "!name")
    public List<Coffee> getAll() {
        return coffeeRepository.findAll(Sort.by("id"));
    }

    @GetMapping(path = "/", params = "name")
    public Coffee getByName(@RequestParam String name) {
        return coffeeRepository.findByName(name);
    }

}
