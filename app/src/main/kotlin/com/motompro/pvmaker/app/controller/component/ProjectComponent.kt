package com.motompro.pvmaker.app.controller.component

import com.motompro.pvmaker.app.PVMakerApp
import com.motompro.pvmaker.app.controller.EditPVController
import com.motompro.pvmaker.app.model.project.Project
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.text.Text
import javafx.stage.Stage

class ProjectComponent {

    @FXML
    private lateinit var projectNumberText: Text
    @FXML
    private lateinit var clientText: Text
    @FXML
    private lateinit var subjectText: Text

    var project: Project? = null
        set(value) {
            field = value
            projectNumberText.text = project?.number
            clientText.text = project?.client
            subjectText.text = project?.subject
        }

    @FXML
    private fun onEditButtonClick() {
        val fxmlLoader = FXMLLoader(PVMakerApp.INSTANCE.getResourceOrLoad("views/edit-pv-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 600.0, 750.0)

        val controller = fxmlLoader.getController<EditPVController>()
        controller.pv = PVMakerApp.INSTANCE.pvManager.getOrCreatePV(project!!)

        val stage = Stage()
        controller.stage = stage
        stage.title = PVMakerApp.WINDOW_TITLE
        stage.scene = scene
        stage.setOnCloseRequest { controller.savePV() }
        stage.show()
    }
}
