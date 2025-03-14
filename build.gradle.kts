plugins {
    java
    id("checkstyle")
}

group = "io.dorum"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.testng/testng
    implementation("org.testng:testng:7.10.2")

    // https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-java
    implementation("org.seleniumhq.selenium:selenium-java:4.22.0")

    // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-slf4j2-impl
    testImplementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.23.1")

    // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")

    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    implementation("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")

    // https://mvnrepository.com/artifact/io.github.bonigarcia/webdrivermanager
    implementation("io.github.bonigarcia:webdrivermanager:5.9.1")

    // https://mvnrepository.com/artifact/com.epam.reportportal/agent-java-testng
    implementation("com.epam.reportportal:agent-java-testng:5.4.2")

    // https://mvnrepository.com/artifact/com.epam.reportportal/logger-java-log4j
    implementation("com.epam.reportportal:logger-java-log4j:5.2.2")

    // https://mvnrepository.com/artifact/com.h2database/h2
    implementation("com.h2database:h2:2.3.230")

    // https://mvnrepository.com/artifact/org.aspectj/aspectjweaver
    runtimeOnly("org.aspectj:aspectjweaver:1.9.22.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

checkstyle {
    toolVersion = "10.12.4"
    config = project.resources.text.fromFile(file("src/main/resources/checkstyle-sun.xml"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    sourceCompatibility = "21"
    targetCompatibility = "21"
}

tasks.test {
    useTestNG {
        suites("src/test/resources/testng.xml")
//        threadCount = 2
    }
    testLogging {
        events("passed", "skipped", "failed")
    }

    doFirst {
        val weaver = configurations.testRuntimeClasspath.get().firstOrNull { it.name.contains("aspectjweaver") }
        weaver?.let {
            println(it.absolutePath)
            jvmArgs = listOf("-javaagent:${it.absolutePath}")
        }
    }
    environment("AGENT_NO_ANALYTICS", "1")
}