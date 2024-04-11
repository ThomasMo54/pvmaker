package com.motompro.pvmaker.app.model.pv

import com.itextpdf.forms.PdfAcroForm
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import com.motompro.pvmaker.app.PVMakerApp
import com.motompro.pvmaker.app.model.project.Project
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val FULL_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE dd LLLL yyyy", Locale.FRANCE)
private val REDUCED_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy")

class PVManager {

    private val emptyPVFile by lazy { PVMakerApp.INSTANCE.getResourceOrLoad("empty_PV.pdf") }
    private val _pvs = mutableMapOf<String, PV>()

    fun getOrCreatePV(project: Project): PV {
        if (_pvs.containsKey(project.number)) return _pvs[project.number]!!
        val defaultData = PVMakerApp.INSTANCE.config.defaultData
        val pv = PV(
            project.number,
            project.client,
            project.contact,
            project.address,
            project.city,
            project.products.getOrNull(0) ?: "",
            project.products.getOrNull(1) ?: "",
            project.number,
            project.contact,
            defaultData.omnisName,
            if (project.endDate != null) FULL_DATE_FORMATTER.format(project.endDate) else "",
            "",
            defaultData.redactedPlace,
            REDUCED_DATE_FORMATTER.format(LocalDateTime.now(ZoneId.systemDefault())),
        )
        _pvs[project.number] = pv
        return pv
    }

    fun createPVFile(pv: PV, targetFile: File) {
        val inputStream = emptyPVFile!!.openStream()
        val outputStream = FileOutputStream(targetFile)
        val buffer = ByteArray(8 * 1024)
        var bytesRead: Int
        while (run { bytesRead = inputStream.read(buffer); return@run bytesRead } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }
        inputStream.close()
        val readerStream = emptyPVFile!!.openStream()
        val document = PdfDocument(PdfReader(readerStream), PdfWriter(targetFile))
        val form = PdfAcroForm.getAcroForm(document, true)
        val fields = form.allFormFields
        fields["number"]?.setValue(pv.number)
        fields["client"]?.setValue(pv.client)
        fields["contact"]?.setValue(pv.contact)
        fields["address"]?.setValue(pv.address)
        fields["city"]?.setValue(pv.city)
        fields["products1"]?.setValue(pv.product1)
        fields["products2"]?.setValue(pv.product2)
        fields["commandNumber"]?.setValue(pv.commandNumber)
        fields["clientName"]?.setValue(pv.clientName)
        fields["omnisName"]?.setValue(pv.omnisName)
        fields["endDate"]?.setValue(pv.endDate)
        fields["observations1"]?.setValue(pv.observations)
        fields["redactedPlace"]?.setValue(pv.redactedPlace)
        fields["redactedDate"]?.setValue(pv.redactedDate)
        readerStream.close()
        outputStream.close()
        document.close()
    }
}
