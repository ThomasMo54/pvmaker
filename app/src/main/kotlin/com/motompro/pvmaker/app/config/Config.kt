package com.motompro.pvmaker.app.config

data class Config(
    var projectsFilePath: String = "",
    val dataColumns: ConfigDataColumns = ConfigDataColumns(),
    val defaultData: ConfigDefaultData = ConfigDefaultData()
)
