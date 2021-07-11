package com.blog.code.handle404;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.security.SecureRandom;

@RestController
@RequestMapping("/api/v1")
public class HelloController {

    @GetMapping("hello")
    public String hello() {
        return "hello";
    }
}
