package com.github.karolhor.ala.discounts.config

import com.github.karolhor.ala.discounts.repository.db.codec.DiscountPolicyWriterConverter
import com.github.karolhor.ala.discounts.repository.db.model.DiscountPolicy
import io.r2dbc.postgresql.codec.EnumCodec
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.Option
import org.springframework.boot.autoconfigure.r2dbc.ConnectionFactoryOptionsBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.PostgresDialect

@Configuration
class DatabaseConfig {
    @Bean
    fun connectionFactoryOptionsBuilderCustomizer(): ConnectionFactoryOptionsBuilderCustomizer =
        ConnectionFactoryOptionsBuilderCustomizer { builder ->
            builder.option(
                Option.valueOf("extensions"),
                listOf(
                    EnumCodec
                        .builder()
                        .withEnum("discount_policy", DiscountPolicy::class.java)
                        .build(),
                ),
            )
        }

    @Bean
    fun r2dbcCustomConversions(connectionFactory: ConnectionFactory): R2dbcCustomConversions =
        R2dbcCustomConversions.of(
            PostgresDialect(),
            listOf(DiscountPolicyWriterConverter()),
        )
}
