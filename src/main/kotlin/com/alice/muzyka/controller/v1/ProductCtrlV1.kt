package com.alice.muzyka.controller.v1

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/product")
class ProductCtrlV1 {

    @GetMapping
    fun getAll() {}

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int) {}

    @GetMapping("/search")
    fun getByParams(
      @RequestParam(required= false) name: String?,
      @RequestParam(required= false) category: String?,
      @RequestParam(required= false) minPrice: Int?,
      @RequestParam(required= false) maxPrice: Int?,
    ) {}

    @PostMapping
    fun post() {}

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int) {}

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int) {}

}