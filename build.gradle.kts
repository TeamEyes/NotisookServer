plugins {
	java
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	//implementation ("com.google.cloud:google-cloud-language:2.21.0")
	//implementation ("com.google.auth:google-auth-library-oauth2-http:1.18.0")
	implementation ("com.google.firebase:firebase-admin:9.2.0")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.bouncycastle:bcprov-jdk15on:1.68")
	implementation ("org.json:json:20230227")
	//implementation("org.springframework.boot:spring-boot-starter-security")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation ("com.opencsv:opencsv:5.8")
	//implementation ("com.google.cloud:google-cloud-aiplatform:3.11.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
