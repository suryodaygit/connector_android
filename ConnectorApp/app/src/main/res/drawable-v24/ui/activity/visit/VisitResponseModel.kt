package com.cmrk.ui.activity.visit

data class VisitResponseModel(
    val data: ArrayList<Data>,
    val message: String,
    val status: Int
)