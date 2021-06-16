package com.example.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@SpringBootApplication
public class JavaApplication {

        @RequestMapping("/")
        @ResponseBody
        String home(){
            return "V30 p*ry";
        }
	public static void main(String[] args) {
		SpringApplication.run(JavaApplication.class, args);
	}

}
