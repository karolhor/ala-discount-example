package com.github.karolhor.ala.discounts.api.mapper

import com.github.karolhor.ala.discounts.api.model.Price
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class PriceApiMapper {
    fun priceToResponse(price: BigDecimal): Price {
        val formattedPrice = String.format("%.2f", price)

        return Price(
            inCents = (price * CENT_FACTOR).toInt(),
            formatted = formattedPrice
        )
    }

    companion object {
        private val CENT_FACTOR = BigDecimal(100)
    }
}
