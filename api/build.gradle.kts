version = "0.1.0"

plugins {
    id("java-library")
}

dependencies {
    labyApi("api")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.google.guava:guava:31.1-jre") // guava-concurrent is missing in api-project
}

labyModProcessor {
    referenceType = net.labymod.gradle.core.processor.ReferenceType.INTERFACE
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
