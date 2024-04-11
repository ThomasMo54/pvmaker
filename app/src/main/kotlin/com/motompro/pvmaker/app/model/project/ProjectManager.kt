package com.motompro.pvmaker.app.model.project

import com.motompro.pvmaker.app.PVMakerApp
import org.dhatim.fastexcel.reader.ReadableWorkbook
import java.io.File
import kotlin.jvm.optionals.getOrNull

private const val PROJECTS_SHEET_NAME = "Projets"
private const val FIRST_PROJECT_ROW = 6L

class ProjectManager(
    projectsFile: File,
) {

    private val _projects = mutableMapOf<String, Project>()
    val projects: Map<String, Project>
        get() = _projects

    init {
        loadProjects(projectsFile)
    }

    private fun loadProjects(projectsFile: File) {
        val dataColumns = PVMakerApp.INSTANCE.config.dataColumns
        val workbook = ReadableWorkbook(projectsFile)
        val projectsSheet = workbook.findSheet(PROJECTS_SHEET_NAME).getOrNull()
            ?: throw IllegalStateException("projects sheet not found")
        projectsSheet.openStream().skip(FIRST_PROJECT_ROW - 1).forEach { row ->
            val projectNumber = row.getCellText(dataColumns.projectNumber) ?: return@forEach
            if (projectNumber.isBlank()) return@forEach
            val client = row.getCellText(dataColumns.client)
            val subject = row.getCellText(dataColumns.subject)
            val isEnded = row.getCellText(dataColumns.isEnded) == "2"
            val isPVSent = !row.getCellText(dataColumns.isPvSent).isNullOrBlank() || !row.getCellText(dataColumns.isPvReceived).isNullOrBlank() || !row.getCellText(dataColumns.isPvFinished).isNullOrBlank()
            val products = listOf(row.getCellText(dataColumns.product1), row.getCellText(dataColumns.product2))
            val address = row.getCellText(dataColumns.address)
            val city = row.getCellText(dataColumns.city)
            val contact = row.getCellText(dataColumns.contact)
            val project = Project(
                projectNumber,
                client,
                subject,
                isEnded,
                if (isEnded) row.getCellAsDate(dataColumns.endDate).getOrNull() else null,
                isPVSent,
                products,
                address,
                city,
                contact,
            )
            _projects[projectNumber] = project
        }
    }
}
