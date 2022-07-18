package com.cmrk.ui.trips

data class PerformanceResponseModel(
    val `data`: ArrayList<Data>,
    val message: String,
    val status: Int
)