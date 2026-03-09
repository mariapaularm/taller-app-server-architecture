package com.microspringboot;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/pi")
    public String getPI() {
        return "PI = " + Math.PI;
    }

    @GetMapping("/hello")
    public String helloW() {
        return "Greetings from Spring Boot!";
    }
}