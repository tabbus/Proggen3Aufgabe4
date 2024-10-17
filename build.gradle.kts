plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.0.14"
}

group = "de.medieninformatik"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
javafx {
    modules("javafx.controls", "javafx.graphics")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}
application {
    mainModule.set("${group}.${rootProject.name}")  // nur für modulare Anwendungen
    mainClass.set("${mainModule.get().lowercase()}.Main")
}

tasks.named<JavaExec>("run") {
    setStandardInput(System.`in`)
    setStandardOutput(System.out)
    setErrorOutput(System.err)
    setIgnoreExitValue(true)
}
tasks.test {
    useJUnitPlatform()
}