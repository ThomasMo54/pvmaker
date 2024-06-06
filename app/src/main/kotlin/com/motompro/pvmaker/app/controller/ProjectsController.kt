package com.motompro.pvmaker.app.controller

import com.motompro.pvmaker.app.PVMakerApp
import com.motompro.pvmaker.app.controller.component.ProjectComponent
import com.motompro.pvmaker.app.model.project.Project
import com.motompro.pvmaker.app.model.project.ProjectManager
import com.motompro.pvmaker.app.model.pv.PV
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.BorderPane
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.awt.Desktop

private val EVEN_CONFIG_NODE_COLOR = Background(BackgroundFill(Color.color(0.95, 0.95, 0.95), CornerRadii.EMPTY, Insets.EMPTY))
private val ODD_CONFIG_NODE_COLOR = Background(BackgroundFill(Color.color(0.9, 0.9, 0.9), CornerRadii.EMPTY, Insets.EMPTY))

class ProjectsController {

    @FXML
    private lateinit var projectsScrollPane: ScrollPane
    @FXML
    private lateinit var projectsVBox: VBox

    private lateinit var inputs: Set<Node>
    @FXML
    private lateinit var numberTextField: TextField
    @FXML
    private lateinit var clientTextField: TextField
    @FXML
    private lateinit var contactTextField: TextField
    @FXML
    private lateinit var addressTextField: TextField
    @FXML
    private lateinit var cityTextField: TextField
    @FXML
    private lateinit var product1TextField: TextField
    @FXML
    private lateinit var product2TextField: TextField
    @FXML
    private lateinit var commandNumberTextField: TextField
    @FXML
    private lateinit var clientNameTextField: TextField
    @FXML
    private lateinit var omnisNameTextField: TextField
    @FXML
    private lateinit var endDateTextField: TextField
    @FXML
    private lateinit var observationsTextField: TextArea
    @FXML
    private lateinit var redactedPlaceTextField: TextField
    @FXML
    private lateinit var redactedDateTextField: TextField
    @FXML
    private lateinit var saveButton: Button

    var projectManager: ProjectManager? = null
        set(value) {
            field = value
            showProjects()
        }
    var pv: PV? = null
        set(value) {
            field = value
            setPrefilledFields()
            if (value != null) {
                inputs.forEach { it.isDisable = false }
            }
        }
    lateinit var stage: Stage

    @FXML
    private fun initialize() {
        projectsVBox.prefWidthProperty().bind(projectsScrollPane.widthProperty())
        inputs = setOf(
            numberTextField,
            clientTextField,
            contactTextField,
            addressTextField,
            cityTextField,
            product1TextField,
            product2TextField,
            commandNumberTextField,
            clientNameTextField,
            omnisNameTextField,
            endDateTextField,
            observationsTextField,
            redactedPlaceTextField,
            redactedDateTextField,
            saveButton,
        )
        inputs.forEach { it.isDisable = true }
    }

    @FXML
    private fun onRegisterButtonClick() {
        val fileChooser = FileChooser()
        fileChooser.initialFileName = "${pv?.number ?: ""} - ${pv?.client}.pdf"
        fileChooser.extensionFilters.add(
            FileChooser.ExtensionFilter(
                "Fichiers PDF (*.pdf)",
                "*.pdf",
            ),
        )
        val file = fileChooser.showSaveDialog(PVMakerApp.INSTANCE.stage) ?: kotlin.run {
            return
        }
        savePV()
        PVMakerApp.INSTANCE.pvManager.createPVFile(pv!!, file)
        stage.close()
        Desktop.getDesktop().open(file)
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
        controller.projectsController = this
        controller.project = project
        projectsVBox.children.add(node)
    }

    private fun setPrefilledFields() {
        numberTextField.text = pv?.number ?: ""
        clientTextField.text = pv?.client ?: ""
        contactTextField.text = pv?.contact ?: ""
        addressTextField.text = pv?.address ?: ""
        cityTextField.text = pv?.city ?: ""
        product1TextField.text = pv?.product1 ?: ""
        product2TextField.text = pv?.product2 ?: ""
        commandNumberTextField.text = pv?.number ?: ""
        clientNameTextField.text = pv?.clientName ?: ""
        omnisNameTextField.text = pv?.omnisName ?: ""
        endDateTextField.text = pv?.endDate
        observationsTextField.text = pv?.observations ?: ""
        redactedPlaceTextField.text = pv?.redactedPlace ?: ""
        redactedDateTextField.text = pv?.redactedDate
    }

    private fun savePV() {
        pv?.number = numberTextField.text
        pv?.client = clientTextField.text
        pv?.contact = contactTextField.text
        pv?.address = addressTextField.text
        pv?.city = cityTextField.text
        pv?.product1 = product1TextField.text
        pv?.product2 = product2TextField.text
        pv?.commandNumber = commandNumberTextField.text
        pv?.clientName = clientNameTextField.text
        pv?.omnisName = omnisNameTextField.text
        pv?.endDate = endDateTextField.text
        pv?.observations = observationsTextField.text
        pv?.redactedPlace = redactedPlaceTextField.text
        pv?.redactedDate = redactedDateTextField.text
    }
}
