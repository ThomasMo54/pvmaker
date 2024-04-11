package com.motompro.pvmaker.app.controller

import com.motompro.pvmaker.app.PVMakerApp
import com.motompro.pvmaker.app.model.project.ProjectManager
import javafx.fxml.FXML
import javafx.stage.FileChooser

class SelectFileController {

    @FXML
    private fun onSelectFileButtonClick() {
        val fileChooser = FileChooser()
        fileChooser.extensionFilters.add(
            FileChooser.ExtensionFilter(
                "Fichiers XLSX (*.xlsx; *.xlsm)",
                "*.xlsx",
                "*.xlsm",
            ),
        )
        val file = fileChooser.showOpenDialog(PVMakerApp.INSTANCE.stage) ?: kotlin.run {
            PVMakerApp.INSTANCE.showErrorAlert("Erreur", "Fichier incorrect")
            return
        }
        PVMakerApp.INSTANCE.config.projectsFilePath = file.absolutePath
        PVMakerApp.INSTANCE.saveConfig()
        val projectManager = ProjectManager(file)
        val projectsController = PVMakerApp.INSTANCE.swapScene<ProjectsController>("views/projects-view.fxml")
        projectsController.projectManager = projectManager
    }
}
