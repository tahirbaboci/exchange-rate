import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
}

group = "com.baboci"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}


object versions {
	const val spring_boot="2.6.2"
	const val kotest="5.1.0"
}


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web-services:${versions.spring_boot}")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jsoup:jsoup:1.14.3")
	implementation("org.springdoc:springdoc-openapi-ui:1.6.4")
	implementation("junit:junit:4.13.1")
	testImplementation("io.kotest:kotest-property:${versions.kotest}")
	testImplementation("org.springframework.boot:spring-boot-starter-test:${versions.spring_boot}")
	testImplementation("io.mockk:mockk:1.12.2")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
