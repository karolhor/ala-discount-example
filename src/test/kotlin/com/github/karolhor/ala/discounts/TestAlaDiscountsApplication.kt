package com.github.karolhor.ala.discounts

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<AlaDiscountsApplication>().with(TestcontainersConfiguration::class).run(*args)
}
