package com.lance.demo.spring.security.conroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController@RequestMapping("/resource")
public class ResourcesController {
    @GetMapping("/{name}")
    public String getResource(@PathVariable String name) {
        return name;
    }
}
