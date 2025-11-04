package com.alice.muzyka.controller.v1

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController 
class TestCtrlV1 {

    @GetMapping("/test")
    fun getHelloWorld(): String {
        return "Hello World"
    }
}