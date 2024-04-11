package com.motompro.pvmaker.app

import com.motompro.pvmaker.app.config.Config
import com.motompro.pvmaker.app.config.ConfigManager
import com.motompro.pvmaker.app.controller.ProjectsController
import com.motompro.pvmaker.app.model.project.ProjectManager
import com.motompro.pvmaker.app.model.pv.PVManager
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.stage.Stage
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import kotlin.system.exitProcess

private const val DEFAULT_WIDTH = 720.0
private const val DEFAULT_HEIGHT = 480.0
private const val MAX_ALERT_MESSAGE_LENGTH = 500

class PVMakerApp : Application() {

    private val resources = mutableMapOf<String, URL>()
    private val configFile = File(
        File(PVMakerApp::class.java.protectionDomain.codeSource.location.path.replace("%20", " ")).parentFile,
        "config.yml",
    )

    lateinit var stage: Stage
    lateinit var config: Config
    val pvManager = PVManager()

    override fun start(stage: Stage) {
        INSTANCE = this
        this.stage = stage
        loadConfig()

        val root: Parent = if (config.projectsFilePath.isBlank()) {
            FXMLLoader(PVMakerApp::class.java.getResource("views/select-file-view.fxml")).load()
        } else {
            val fxmlLoader = FXMLLoader(PVMakerApp::class.java.getResource("views/projects-view.fxml"))
            val root = fxmlLoader.load<Parent>()
            val controller: ProjectsController = fxmlLoader.getController()
            val projectManager = ProjectManager(File(config.projectsFilePath))
            controller.projectManager = projectManager
            root
        }
        val scene = Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT)
        stage.title = WINDOW_TITLE
        stage.scene = scene
        stage.setOnCloseRequest {
            Platform.exit()
            exitProcess(0)
        }
        stage.show()
    }

    private fun loadConfig() {
        if (!configFile.exists()) {
            configFile.createNewFile()
            val defaultConfig = PVMakerApp::class.java.getResource("config.yml")
            val inputStream = defaultConfig!!.openStream()
            val outputStream = FileOutputStream(configFile)
            val buffer = ByteArray(8 * 1024)
            var bytesRead: Int
            while (run { bytesRead = inputStream.read(buffer); return@run bytesRead } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            inputStream.close()
            outputStream.close()
        }
        config = ConfigManager.loadFromFile(configFile, Config::class.java)
    }

    fun saveConfig() {
        ConfigManager.writeToFile(configFile, config)
    }

    fun showErrorAlert(title: String, message: String) {
        val errorAlert = Alert(Alert.AlertType.ERROR)
        errorAlert.title = title
        errorAlert.headerText = title
        errorAlert.contentText = message.substring(0, Integer.min(message.length, MAX_ALERT_MESSAGE_LENGTH))
        errorAlert.dialogPane.minWidth = 500.0
        errorAlert.showAndWait()
    }

    fun showInfoAlert(title: String, message: String) {
        val infoAlert = Alert(Alert.AlertType.INFORMATION)
        infoAlert.title = title
        infoAlert.headerText = title
        infoAlert.contentText = message.substring(0, Integer.min(message.length, MAX_ALERT_MESSAGE_LENGTH))
        infoAlert.dialogPane.minWidth = 500.0
        infoAlert.showAndWait()
    }

    fun swapScene(sceneView: String) {
        val fxmlLoader = FXMLLoader(getResourceOrLoad(sceneView))
        val scene = Scene(fxmlLoader.load(), stage.scene.width, stage.scene.height)
        stage.scene = scene
    }

    fun <T> swapScene(sceneView: String): T {
        val fxmlLoader = FXMLLoader(getResourceOrLoad(sceneView))
        val parent = fxmlLoader.load<Parent>()
        val scene = Scene(parent, stage.scene.width, stage.scene.height)
        stage.scene = scene
        return fxmlLoader.getController()
    }

    fun getResourceOrLoad(resourceName: String): URL? {
        if (resources.containsKey(resourceName)) return resources[resourceName]!!
        val resource = PVMakerApp::class.java.getResource(resourceName) ?: return null
        resources[resourceName] = resource
        return resource
    }

    companion object {
        const val WINDOW_TITLE = "PVMaker"
        lateinit var INSTANCE: PVMakerApp
            private set
    }
}

fun main() {
    Application.launch(PVMakerApp::class.java)
}
