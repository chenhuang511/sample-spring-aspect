package vn.psvm.demo.aspectlogging.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.psvm.demo.aspectlogging.LogExecutionTime;
import vn.psvm.demo.aspectlogging.CustomServiceAnnotation;
import vn.psvm.demo.aspectlogging.services.HelloService;

@RestController
@RequestMapping("/api")
public class HelloResources {

    @Autowired
    private HelloService helloService;

    @GetMapping("/hello/1")
    @LogExecutionTime
    public ResponseEntity<?> helloBro(@RequestParam String name, @RequestParam String country) {
        String result = "Hello, " + name + " from " + country + "!\n You are welcome.";
        String decoratedResult = helloService.decorateInput(result);
        return ResponseEntity.ok(decoratedResult);
    }

    @PostMapping("/hello/2")
    @CustomServiceAnnotation
    @LogExecutionTime
    public ResponseEntity<?> processString(@RequestParam String input) {
        return ResponseEntity.ok(input);
    }
}
