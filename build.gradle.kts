group = "com.motompro"
version = "1.0.0"

tasks.register<Copy>("addExecutablesToDistribution") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(
        // App executable
        project(":app").layout.buildDirectory.dir("launch4j"),
        // Updater executable
        project(":updater").layout.buildDirectory.dir("launch4j"),
        // JRE
        zipTree("jre/win_jre_17.zip"),
    )

    into(layout.buildDirectory.dir("dist"))
}

tasks.register<Zip>("packageDistribution") {
    from(layout.buildDirectory.dir("dist"))

    archiveFileName = "PVMaker.zip"
    destinationDirectory = layout.buildDirectory.dir("package")
}

tasks["addExecutablesToDistribution"].dependsOn(":app:createExe")
tasks["addExecutablesToDistribution"].dependsOn(":updater:createExe")

tasks["packageDistribution"].dependsOn("addExecutablesToDistribution")
