package com.motompro.pvmaker.app.model.project

import java.time.LocalDateTime

data class Project(
    val number: String,
    val client: String,
    val subject: String,
    val isEnded: Boolean,
    val endDate: LocalDateTime?,
    val isPVSent: Boolean,
)
