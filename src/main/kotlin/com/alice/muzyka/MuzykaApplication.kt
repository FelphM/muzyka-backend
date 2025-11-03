package com.alice.muzyka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MuzykaApplication

fun main(args: Array<String>) {
	runApplication<MuzykaApplication>(*args)
}
