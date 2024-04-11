package com.motompro.pvmaker.app.controller

import com.motompro.pvmaker.app.PVMakerApp
import com.motompro.pvmaker.app.model.pv.PV
import javafx.fxml.FXML
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.awt.Desktop

class EditPVController {

    @FXML
    lateinit var numberTextField: TextField
    @FXML
    lateinit var clientTextField: TextField
    @FXML
    lateinit var contactTextField: TextField
    @FXML
    lateinit var addressTextField: TextField
    @FXML
    lateinit var cityTextField: TextField
    @FXML
    lateinit var product1TextField: TextField
    @FXML
    lateinit var product2TextField: TextField
    @FXML
    lateinit var commandNumberTextField: TextField
    @FXML
    lateinit var clientNameTextField: TextField
    @FXML
    lateinit var omnisNameTextField: TextField
    @FXML
    lateinit var endDateTextField: TextField
    @FXML
    lateinit var observationsTextField: TextArea
    @FXML
    lateinit var redactedPlaceTextField: TextField
    @FXML
    lateinit var redactedDateTextField: TextField

    var pv: PV? = null
        set(value) {
            field = value
            setPrefilledFields()
        }
    lateinit var stage: Stage

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

    fun savePV() {
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

    @FXML
    private fun onRegisterButtonClick() {
        val fileChooser = FileChooser()
        fileChooser.initialFileName = "PV_${pv?.number ?: ""}.pdf"
        fileChooser.extensionFilters.add(
            FileChooser.ExtensionFilter(
                "Fichiers PDF (*.pdf)",
                "*.pdf",
            ),
        )
        val file = fileChooser.showSaveDialog(PVMakerApp.INSTANCE.stage) ?: kotlin.run {
            PVMakerApp.INSTANCE.showErrorAlert("Erreur", "Fichier incorrect")
            return
        }
        savePV()
        PVMakerApp.INSTANCE.pvManager.createPVFile(pv!!, file)
        stage.close()
        Desktop.getDesktop().open(file)
    }
}
