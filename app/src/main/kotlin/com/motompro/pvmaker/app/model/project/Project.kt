package com.motompro.pvmaker.app.model.project

import java.time.LocalDateTime

data class Project(
    val number: String,
    val client: String,
    val subject: String,
    val isEnded: Boolean,
    val endDate: LocalDateTime?,
    val isPVSent: Boolean,
    val products: List<String>,
    val address: String,
    val city: String,
    val contact: String,
)
