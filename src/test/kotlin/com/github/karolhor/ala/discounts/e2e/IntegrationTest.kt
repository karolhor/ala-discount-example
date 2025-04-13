package com.github.karolhor.ala.discounts.e2e

import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@AutoConfigureWebTestClient
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Testcontainers
abstract class IntegrationTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var entityTemplate: R2dbcEntityTemplate

    @AfterEach
    fun tearDown() {
        runBlocking {
            val databaseClient = entityTemplate.databaseClient

            val tableNames =
                databaseClient
                    .sql(
                        """
                        SELECT tablename FROM pg_tables WHERE schemaname = 'public'
                        """.trimIndent(),
                    ).map { row, _ -> row.get("tablename", String::class.java)!! }
                    .all()
                    .collectList()
                    .awaitSingle()

            databaseClient.sql("SET session_replication_role = replica").then().awaitSingleOrNull()

            tableNames.forEach { table ->
                databaseClient.sql("TRUNCATE TABLE \"$table\" CASCADE").then().awaitSingleOrNull()
            }

            databaseClient.sql("SET session_replication_role = origin").then().awaitSingleOrNull()
        }
    }

    companion object {
        @Container
        private val postgresContainer =
            PostgreSQLContainer(DockerImageName.parse("postgres:17.4-alpine"))
                .withInitScript("sql/schema.sql")

        @JvmStatic
        @DynamicPropertySource
        fun dbProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.r2dbc.url") { postgresContainer.jdbcUrl.replace("jdbc", "r2dbc") }
            registry.add("spring.r2dbc.username") { postgresContainer.username }
            registry.add("spring.r2dbc.password") { postgresContainer.password }
        }
    }
}
