import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.bmuschko.docker-remote-api") version "9.4.0"
	id("com.avast.gradle.docker-compose") version "0.17.12"
}

group = "com.github.karolhor"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.postgresql:postgresql")
	implementation("org.postgresql:r2dbc-postgresql")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.28.1")
	testImplementation("io.mockk:mockk:1.14.0")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}


tasks.register<DockerBuildImage>("dockerBuildImage") {
	group = "docker"
	inputDir.set(project.projectDir)
	dockerFile.set(project.file("deploy/Dockerfile"))
	images.add("karolhor/ala-discounts:latest")
}

dockerCompose {
	listOf("/usr/bin/docker","/usr/local/bin/docker", "/opt/homebrew/bin/docker").firstOrNull {
		File(it).exists()
	}?.let { docker ->
		// works around an issue where the docker
		// command is not found
		// falls back to the default, which may work on
		// some platforms
		dockerExecutable.set(docker)
	}

	listOf("/usr/bin/docker-compose","/usr/local/bin/docker-compose", "/opt/homebrew/bin/docker-compose").firstOrNull {
		File(it).exists()
	}?.let { dockerCompose ->
		// works around an issue where the docker-compose
		// command is not found
		// falls back to the default, which may work on
		// some platforms
		executable.set(dockerCompose)
		useDockerComposeV2 = false
	}

	useComposeFiles.set(listOf("${rootDir}/docker-compose.yml"))
}
