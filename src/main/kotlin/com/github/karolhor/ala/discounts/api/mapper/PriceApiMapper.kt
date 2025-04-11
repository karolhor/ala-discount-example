package com.github.karolhor.ala.discounts.api.mapper

import com.github.karolhor.ala.discounts.api.model.Price
import org.springframework.stereotype.Component

@Component
class PriceApiMapper {
    fun priceToResponse(priceInCents: Int): Price {
        val price = priceInCents.toDouble() / CENT_FACTOR
        val formattedPrice = price.to2DecimalString()

        return Price(
            inCents = priceInCents,
            formatted = formattedPrice
        )
    }

    private fun Double.to2DecimalString(): String = String.format("%.2f", this)

    companion object {
        private const val CENT_FACTOR = 100
    }
}
