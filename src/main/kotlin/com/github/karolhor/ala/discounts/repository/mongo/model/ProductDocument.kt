package com.github.karolhor.ala.discounts.repository.mongo.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "products")
data class ProductDocument(
    @Id
    val id: String,
    val name: String,
    val description: String,
    val price: Int
)
