package com.motompro.pvmaker.app.controller

import com.motompro.pvmaker.app.PVMakerApp
import com.motompro.pvmaker.app.controller.component.ProjectComponent
import com.motompro.pvmaker.app.model.project.Project
import com.motompro.pvmaker.app.model.project.ProjectManager
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.Insets
import javafx.scene.control.ScrollPane
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.BorderPane
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.VBox
import javafx.scene.paint.Color

private val EVEN_CONFIG_NODE_COLOR = Background(BackgroundFill(Color.color(0.95, 0.95, 0.95), CornerRadii.EMPTY, Insets.EMPTY))
private val ODD_CONFIG_NODE_COLOR = Background(BackgroundFill(Color.color(0.9, 0.9, 0.9), CornerRadii.EMPTY, Insets.EMPTY))

class ProjectsController {

    @FXML
    private lateinit var projectsScrollPane: ScrollPane
    @FXML
    private lateinit var projectsVBox: VBox

    var projectManager: ProjectManager? = null
        set(value) {
            field = value
            showProjects()
        }

    @FXML
    private fun initialize() {
        projectsVBox.prefWidthProperty().bind(projectsScrollPane.widthProperty())
    }

    private fun showProjects() {
        var even = false
        projectManager?.projects?.values
            ?.filter { it.isEnded && !it.isPVSent }
            ?.forEach {
                addProjectComponent(it, if (even) EVEN_CONFIG_NODE_COLOR else ODD_CONFIG_NODE_COLOR)
                even = !even
            }
    }

    private fun addProjectComponent(project: Project, background: Background) {
        val fxmlLoader = FXMLLoader(PVMakerApp::class.java.getResource("components/project-component.fxml"))
        val node = fxmlLoader.load<BorderPane>()
        node.background = background
        node.prefWidthProperty().bind(projectsVBox.widthProperty())
        val controller = fxmlLoader.getController<ProjectComponent>()
        controller.project = project
        projectsVBox.children.add(node)
    }
}
