plugins {
    `java-library`
}

dependencies {
    implementation("com.github.spotbugs:spotbugs-annotations:4.5.3")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

