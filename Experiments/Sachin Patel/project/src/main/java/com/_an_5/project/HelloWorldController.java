package com._an_5.project;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/")
    public String helloworld() {return "Hello User";}

    @GetMapping("/{title}")
    public String welcome(@PathVariable String title) {
        return "Hello " + title;
    }
}
