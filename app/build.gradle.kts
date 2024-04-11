plugins {
    id("java")
    kotlin("jvm") version "1.9.21"
    id("application")
    id("org.javamodularity.moduleplugin").version("1.8.12")
    id("org.openjfx.javafxplugin").version("0.0.13")
    id("edu.sc.seis.launch4j").version("3.0.4")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.yaml:snakeyaml:2.2")
    implementation("org.dhatim:fastexcel:0.17.0")
    implementation("org.dhatim:fastexcel-reader:0.17.0")
    implementation("com.itextpdf:itext-core:8.0.2@pom")
    implementation("com.itextpdf:cleanup:4.0.2")
    implementation("com.itextpdf:forms:8.0.2")
}

application {
    mainModule = "com.motompro.pvmaker.app"
    mainClass = "com.motompro.pvmaker.app.PVMakerApp"
}

tasks {
    withType<ProcessResources> {
        val versionFile = File(rootDir, "version.txt")
        versionFile.delete()
        versionFile.createNewFile()
        versionFile.writeText("${rootProject.version}")
        from(versionFile)
    }
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

javafx {
    version = "17.0.6"
    modules = listOf("javafx.controls", "javafx.fxml")
}

launch4j {
    outfile = "PVMaker.exe"
    bundledJrePath = "jre"
    jreMinVersion = "17"
    mainClassName = "com.motompro.pvmaker.app.PVMakerAppKt"
    productName = "PVMaker"
    setJarTask(project.tasks["jar"])
}
