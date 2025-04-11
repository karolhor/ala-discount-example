package com.github.karolhor.ala.discounts.config

import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter

@Configuration
class MongoConfiguration(
    private val mappingMongoConverter: MappingMongoConverter
) : InitializingBean {
    override fun afterPropertiesSet() {
        // remove _class field from document
        mappingMongoConverter.setTypeMapper(DefaultMongoTypeMapper(null))
    }
}
