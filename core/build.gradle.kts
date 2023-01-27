version = "0.1.0"

plugins {
    id("java-library")
}

dependencies {
    api(project(":api"))
    implementation("org.sejda.imageio:webp-imageio:0.1.6")
}

labyModProcessor {
    referenceType = net.labymod.gradle.core.processor.ReferenceType.DEFAULT
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}