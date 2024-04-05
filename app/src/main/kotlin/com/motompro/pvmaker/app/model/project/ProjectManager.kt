package com.motompro.pvmaker.app.model.project

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
        val workbook = ReadableWorkbook(projectsFile)
        val projectsSheet = workbook.findSheet(PROJECTS_SHEET_NAME).getOrNull()
            ?: throw IllegalStateException("projects sheet not found")
        projectsSheet.openStream().skip(FIRST_PROJECT_ROW - 1).forEach { row ->
            val projectNumber = row.getCellText(1) ?: return@forEach
            if (projectNumber.isBlank()) return@forEach
            val client = row.getCellText(5)
            val subject = row.getCellText(8)
            val isEnded = row.getCellText(18) == "2"
            val isPVSent = !row.getCellText(20).isNullOrBlank() || !row.getCellText(21).isNullOrBlank() || !row.getCellText(22).isNullOrBlank()
            val project = Project(
                projectNumber,
                client,
                subject,
                isEnded,
                if (isEnded) row.getCellAsDate(19).getOrNull() else null,
                isPVSent,
            )
            _projects[projectNumber] = project
        }
    }
}
