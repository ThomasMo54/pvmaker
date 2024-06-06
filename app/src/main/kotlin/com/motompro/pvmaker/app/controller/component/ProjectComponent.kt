package com.motompro.pvmaker.app.controller.component

import com.motompro.pvmaker.app.PVMakerApp
import com.motompro.pvmaker.app.controller.ProjectsController
import com.motompro.pvmaker.app.model.project.Project
import javafx.fxml.FXML
import javafx.scene.text.Text

class ProjectComponent {

    @FXML
    private lateinit var projectNumberText: Text
    @FXML
    private lateinit var clientText: Text
    @FXML
    private lateinit var subjectText: Text

    var projectsController: ProjectsController? = null
    var project: Project? = null
        set(value) {
            field = value
            projectNumberText.text = project?.number
            clientText.text = project?.client
            subjectText.text = project?.subject
        }

    @FXML
    private fun onEditButtonClick() {
        projectsController?.pv = PVMakerApp.INSTANCE.pvManager.getOrCreatePV(project!!)
    }
}
