package org.orderhub.st

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StApplication

fun main(args: Array<String>) {
    runApplication<StApplication>(*args)
}
