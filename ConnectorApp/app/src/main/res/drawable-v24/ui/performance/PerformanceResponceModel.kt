package com.cmrk.ui.performance

data class PerformanceResponceModel(
    val `data`: List<Data>,
    val message: String,
    val status: Int
)