package com.motompro.pvmaker.app

import com.motompro.pvmaker.app.model.pv.PVManager
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.stage.Stage
import java.net.URL
import kotlin.system.exitProcess

private const val DEFAULT_WIDTH = 720.0
private const val DEFAULT_HEIGHT = 480.0
private const val MAX_ALERT_MESSAGE_LENGTH = 500

class PVMakerApp : Application() {

    private val resources = mutableMapOf<String, URL>()

    lateinit var stage: Stage
    val pvManager = PVManager()

    override fun start(stage: Stage) {
        INSTANCE = this
        this.stage = stage

        val resource = PVMakerApp::class.java.getResource("views/select-file-view.fxml")
        if (resource != null) resources["views/select-file-view.fxml"] = resource
        val fxmlLoader = FXMLLoader(resource)
        val scene = Scene(fxmlLoader.load(), DEFAULT_WIDTH, DEFAULT_HEIGHT)
        stage.title = WINDOW_TITLE
        stage.scene = scene
        stage.setOnCloseRequest {
            Platform.exit()
            exitProcess(0)
        }
        stage.show()
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
