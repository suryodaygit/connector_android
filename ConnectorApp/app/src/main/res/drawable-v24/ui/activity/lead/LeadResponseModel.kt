package com.cmrk.ui.activity.lead

data class LeadResponseModel(
    val `data`: ArrayList<Data>,
    val message: String,
    val status: Int
)