import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.3.61"
	maven
}

group = "com.github"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenLocal()
	mavenCentral()
	maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.squareup:kotlinpoet:1.5.0")
	implementation("com.github:kotlin-poet-dsl:0.0.1-SNAPSHOT")
	implementation( "org.springframework.restdocs:spring-restdocs-webtestclient:2.0.4.RELEASE")

	gradleApi()
	implementation(gradleTestKit())

	testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
	testImplementation("org.junit.jupiter:junit-jupiter-params:5.6.0")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		jvmTarget = "1.8"
	}
}
