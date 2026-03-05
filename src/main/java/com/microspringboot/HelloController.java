package com.microspringboot;

@RestController
public class HelloController {

	@GetMapping("/")
	public static String index() {
		return "Greetings from Spring Boot!";
	}
	@GetMapping("/PI")
	public static String getPI() {
		return "PI = " + Math.PI;
	}
	@GetMapping("/HELLO")
	public static String helloW() {
		return "Greetings from Spring Boot!";
	}

}