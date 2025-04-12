package com.github.karolhor.ala.discounts.api.mapper

import java.math.BigDecimal

fun BigDecimal.toPriceString() = String.format("%.2f", this)
