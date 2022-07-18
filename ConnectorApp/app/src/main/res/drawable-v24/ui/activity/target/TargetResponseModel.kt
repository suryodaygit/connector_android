package com.cmrk.ui.activity.target

data class TargetResponseModel(
    val data: ArrayList<Data>,
    val message: String,
    val status: Int
)